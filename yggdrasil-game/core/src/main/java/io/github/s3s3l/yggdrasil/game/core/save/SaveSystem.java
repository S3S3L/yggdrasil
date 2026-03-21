package io.github.s3s3l.yggdrasil.game.core.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.s3s3l.yggdrasil.game.core.scene.Scene;
import io.github.s3s3l.yggdrasil.game.core.ui.scene.SceneState;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存档系统
 * 管理多个存档槽，支持场景状态的保存和加载
 */
public class SaveSystem {

    private static final String DEFAULT_SAVE_DIR = "saves";
    private static final int DEFAULT_SLOT_COUNT = 3;

    private final Map<Integer, SaveSlot> saveSlots;
    private final int maxSlots;
    private final ObjectMapper objectMapper;
    private File saveDirectory;

    /**
     * 创建存档系统（默认 3 个存档位）
     */
    public SaveSystem() {
        this(DEFAULT_SLOT_COUNT);
    }

    /**
     * 创建存档系统
     *
     * @param maxSlots 最大存档位数量
     */
    public SaveSystem(int maxSlots) {
        this.maxSlots = maxSlots;
        this.saveSlots = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        this.saveDirectory = new File(DEFAULT_SAVE_DIR);

        // 初始化存档槽
        for (int i = 1; i <= maxSlots; i++) {
            saveSlots.put(i, new SaveSlot(i));
        }

        // 确保存档目录存在
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
    }

    /**
     * 获取存档目录
     *
     * @return 存档目录
     */
    public File getSaveDirectory() {
        return saveDirectory;
    }

    /**
     * 设置存档目录
     *
     * @param saveDirectory 存档目录
     */
    public void setSaveDirectory(File saveDirectory) {
        this.saveDirectory = saveDirectory;
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
    }

    /**
     * 获取最大存档位数量
     *
     * @return 最大存档位数量
     */
    public int getMaxSlots() {
        return maxSlots;
    }

    /**
     * 获取所有存档槽
     *
     * @return 存档槽集合
     */
    public Collection<SaveSlot> getAllSaveSlots() {
        return Collections.unmodifiableCollection(saveSlots.values());
    }

    /**
     * 获取存档槽
     *
     * @param slotId 存档槽 ID
     * @return 存档槽，如果不存在则返回 null
     */
    public SaveSlot getSaveSlot(int slotId) {
        return saveSlots.get(slotId);
    }

    /**
     * 保存场景到指定存档槽
     *
     * @param slotId 存档槽 ID
     * @param scene  场景
     * @return true 如果保存成功
     */
    public boolean saveScene(int slotId, Scene scene) {
        if (scene == null || !isValidSlotId(slotId)) {
            return false;
        }

        SaveSlot slot = saveSlots.get(slotId);
        slot.setSceneName(scene.getClass().getSimpleName());
        slot.updateTimestamp();

        // 保存场景状态
        SceneState state = new SceneState(scene.getClass().getSimpleName());
        onSaveSceneState(scene, state);

        // 将状态数据复制到存档槽
        slot.getAllSceneData().clear();
        state.getAll().forEach(slot::putSceneData);

        // 保存到文件
        return saveToFile(slot);
    }

    /**
     * 保存场景到指定存档槽
     *
     * @param slotId    存档槽 ID
     * @param slotName  存档名称
     * @param scene     场景
     * @param gameTime  游戏时间
     * @param metadata  元数据
     * @return true 如果保存成功
     */
    public boolean saveScene(int slotId, String slotName, Scene scene, float gameTime, Map<String, String> metadata) {
        if (scene == null || !isValidSlotId(slotId)) {
            return false;
        }

        SaveSlot slot = saveSlots.get(slotId);
        slot.setSlotName(slotName);
        slot.setSceneName(scene.getClass().getSimpleName());
        slot.setGameTime(gameTime);
        slot.updateTimestamp();

        // 保存元数据
        if (metadata != null) {
            metadata.forEach(slot::putMetadata);
        }

        // 保存场景状态
        SceneState state = new SceneState(scene.getClass().getSimpleName());
        onSaveSceneState(scene, state);

        // 将状态数据复制到存档槽
        slot.getAllSceneData().clear();
        state.getAll().forEach(slot::putSceneData);

        // 保存到文件
        return saveToFile(slot);
    }

    /**
     * 从指定存档槽加载场景
     *
     * @param slotId 存档槽 ID
     * @param scene  目标场景
     * @return true 如果加载成功
     */
    public boolean loadScene(int slotId, Scene scene) {
        if (scene == null || !isValidSlotId(slotId)) {
            return false;
        }

        SaveSlot slot = saveSlots.get(slotId);
        if (!slot.hasData()) {
            // 尝试从文件加载
            if (!loadFromFile(slot)) {
                return false;
            }
        }

        // 加载场景状态
        SceneState state = new SceneState(slot.getSceneName());
        slot.getAllSceneData().forEach(state::put);

        onLoadSceneState(scene, state);
        return true;
    }

    /**
     * 获取存档槽信息（不加载完整数据）
     *
     * @param slotId 存档槽 ID
     * @return 存档槽信息，如果为空则返回 null
     */
    public SaveSlotInfo getSaveSlotInfo(int slotId) {
        if (!isValidSlotId(slotId)) {
            return null;
        }

        SaveSlot slot = saveSlots.get(slotId);

        // 尝试从文件加载元数据
        File saveFile = new File(saveDirectory, "save_" + slotId + ".json");
        if (saveFile.exists()) {
            try {
                Map<String, Object> data = objectMapper.readValue(saveFile, Map.class);
                Map<String, Object> infoData = (Map<String, Object>) data.getOrDefault("info", new HashMap<>());

                return new SaveSlotInfo(
                        slotId,
                        (String) infoData.get("slotName"),
                        (String) infoData.get("sceneName"),
                        ((Number) infoData.getOrDefault("timestamp", 0)).longValue(),
                        ((Number) infoData.getOrDefault("gameTime", 0)).floatValue()
                );
            } catch (IOException e) {
                // 忽略读取错误
            }
        }

        if (slot.hasData()) {
            return new SaveSlotInfo(
                    slotId,
                    slot.getSlotName(),
                    slot.getSceneName(),
                    slot.getTimestamp(),
                    slot.getGameTime()
            );
        }

        return null;
    }

    /**
     * 删除指定存档槽的数据
     *
     * @param slotId 存档槽 ID
     * @return true 如果删除成功
     */
    public boolean deleteSave(int slotId) {
        if (!isValidSlotId(slotId)) {
            return false;
        }

        SaveSlot slot = saveSlots.get(slotId);
        slot.clear();

        File saveFile = new File(saveDirectory, "save_" + slotId + ".json");
        if (saveFile.exists()) {
            return saveFile.delete();
        }

        return true;
    }

    /**
     * 检查存档槽是否有数据
     *
     * @param slotId 存档槽 ID
     * @return true 如果有数据
     */
    public boolean hasSave(int slotId) {
        if (!isValidSlotId(slotId)) {
            return false;
        }

        SaveSlot slot = saveSlots.get(slotId);
        if (slot.hasData()) {
            return true;
        }

        // 检查文件是否存在
        File saveFile = new File(saveDirectory, "save_" + slotId + ".json");
        return saveFile.exists();
    }

    /**
     * 获取有数据的存档槽数量
     *
     * @return 有数据的存档槽数量
     */
    public int getSaveCount() {
        int count = 0;
        for (SaveSlot slot : saveSlots.values()) {
            if (hasSave(slot.getSlotId())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取所有有数据的存档槽信息
     *
     * @return 存档槽信息列表
     */
    public List<SaveSlotInfo> getAllSaveInfo() {
        List<SaveSlotInfo> saves = new ArrayList<>();
        for (int i = 1; i <= maxSlots; i++) {
            SaveSlotInfo info = getSaveSlotInfo(i);
            if (info != null) {
                saves.add(info);
            }
        }
        return saves;
    }

    /**
     * 保存存档槽到文件
     *
     * @param slot 存档槽
     * @return true 如果保存成功
     */
    private boolean saveToFile(SaveSlot slot) {
        try {
            File saveFile = new File(saveDirectory, "save_" + slot.getSlotId() + ".json");

            Map<String, Object> data = new HashMap<>();

            // 保存信息部分
            Map<String, Object> info = new HashMap<>();
            info.put("slotId", slot.getSlotId());
            info.put("slotName", slot.getSlotName());
            info.put("sceneName", slot.getSceneName());
            info.put("timestamp", slot.getTimestamp());
            info.put("gameTime", slot.getGameTime());
            info.put("metadata", slot.getAllMetadata());
            data.put("info", info);

            // 保存场景数据部分
            data.put("sceneData", slot.getAllSceneData());

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从文件加载存档槽
     *
     * @param slot 存档槽
     * @return true 如果加载成功
     */
    private boolean loadFromFile(SaveSlot slot) {
        try {
            File saveFile = new File(saveDirectory, "save_" + slot.getSlotId() + ".json");
            if (!saveFile.exists()) {
                return false;
            }

            Map<String, Object> data = objectMapper.readValue(saveFile, Map.class);

            // 加载信息部分
            Map<String, Object> info = (Map<String, Object>) data.getOrDefault("info", new HashMap<>());
            slot.setSlotName((String) info.get("slotName"));
            slot.setSceneName((String) info.get("sceneName"));
            slot.setTimestamp(((Number) info.getOrDefault("timestamp", 0)).longValue());
            slot.setGameTime(((Number) info.getOrDefault("gameTime", 0)).floatValue());

            Map<String, String> metadata = (Map<String, String>) info.getOrDefault("metadata", new HashMap<>());
            metadata.forEach(slot::putMetadata);

            // 加载场景数据部分
            Map<String, Object> sceneData = (Map<String, Object>) data.getOrDefault("sceneData", new HashMap<>());
            sceneData.forEach(slot::putSceneData);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查存档槽 ID 是否有效
     *
     * @param slotId 存档槽 ID
     * @return true 如果有效
     */
    private boolean isValidSlotId(int slotId) {
        return slotId >= 1 && slotId <= maxSlots;
    }

    /**
     * 保存场景状态时的回调
     * 子类可重写以实现自定义保存逻辑
     *
     * @param scene 场景
     * @param state 场景状态
     */
    protected void onSaveSceneState(Scene scene, SceneState state) {
        // 子类可重写
    }

    /**
     * 加载场景状态时的回调
     * 子类可重写以实现自定义加载逻辑
     *
     * @param scene 场景
     * @param state 场景状态
     */
    protected void onLoadSceneState(Scene scene, SceneState state) {
        // 子类可重写
    }

    /**
     * 存档槽信息类
     */
    public static class SaveSlotInfo {
        private final int slotId;
        private final String slotName;
        private final String sceneName;
        private final long timestamp;
        private final float gameTime;

        public SaveSlotInfo(int slotId, String slotName, String sceneName, long timestamp, float gameTime) {
            this.slotId = slotId;
            this.slotName = slotName;
            this.sceneName = sceneName;
            this.timestamp = timestamp;
            this.gameTime = gameTime;
        }

        public int getSlotId() {
            return slotId;
        }

        public String getSlotName() {
            return slotName;
        }

        public String getSceneName() {
            return sceneName;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public float getGameTime() {
            return gameTime;
        }

        @Override
        public String toString() {
            return "SaveSlotInfo{" +
                    "slotId=" + slotId +
                    ", slotName='" + slotName + '\'' +
                    ", sceneName='" + sceneName + '\'' +
                    ", timestamp=" + timestamp +
                    ", gameTime=" + gameTime +
                    '}';
        }
    }
}
