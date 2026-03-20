package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import java.util.HashMap;
import java.util.Map;

/**
 * 场景状态管理器
 * 管理场景状态的保存和加载
 */
public class SceneStateManager {

    private static SceneStateManager instance;

    private final Map<String, SceneState> states;
    private String lastSavedStateName;

    /**
     * 私有构造函数
     */
    private SceneStateManager() {
        this.states = new HashMap<>();
    }

    /**
     * 获取单例实例
     *
     * @return SceneStateManager 实例
     */
    public static synchronized SceneStateManager getInstance() {
        if (instance == null) {
            instance = new SceneStateManager();
        }
        return instance;
    }

    /**
     * 重置单例（用于测试）
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.states.clear();
            instance.lastSavedStateName = null;
        }
        instance = null;
    }

    /**
     * 保存场景状态
     *
     * @param scene 场景
     * @return 保存的状态
     */
    public SceneState save(UIScene scene) {
        if (scene == null || scene.getName() == null) {
            return null;
        }

        SceneState state = new SceneState(scene.getName());
        onSaveState(scene, state);

        states.put(scene.getName(), state);
        lastSavedStateName = scene.getName();

        return state;
    }

    /**
     * 保存场景状态
     *
     * @param sceneName 场景名称
     * @return 保存的状态
     */
    public SceneState save(String sceneName) {
        if (sceneName == null) {
            return null;
        }

        SceneState state = new SceneState(sceneName);
        states.put(sceneName, state);
        lastSavedStateName = sceneName;

        return state;
    }

    /**
     * 加载场景状态
     *
     * @param sceneName 场景名称
     * @return 场景状态，如果没有则返回 null
     */
    public SceneState load(String sceneName) {
        return states.get(sceneName);
    }

    /**
     * 加载场景状态到场景
     *
     * @param scene 场景
     */
    public void loadToScene(UIScene scene) {
        if (scene == null || scene.getName() == null) {
            return;
        }

        SceneState state = states.get(scene.getName());
        if (state != null) {
            onRestoreState(scene, state);
        }
    }

    /**
     * 获取最后保存的状态名称
     *
     * @return 状态名称
     */
    public String getLastSavedStateName() {
        return lastSavedStateName;
    }

    /**
     * 检查是否有保存的状态
     *
     * @param sceneName 场景名称
     * @return true 如果有保存的状态
     */
    public boolean hasState(String sceneName) {
        return states.containsKey(sceneName);
    }

    /**
     * 移除场景状态
     *
     * @param sceneName 场景名称
     */
    public void removeState(String sceneName) {
        states.remove(sceneName);
    }

    /**
     * 清除所有状态
     */
    public void clearAll() {
        states.clear();
        lastSavedStateName = null;
    }

    /**
     * 获取所有保存的状态
     *
     * @return 状态映射
     */
    public Map<String, SceneState> getAllStates() {
        return new HashMap<>(states);
    }

    /**
     * 保存状态时的回调
     * 子类可重写以实现自定义保存逻辑
     *
     * @param scene 场景
     * @param state 状态
     */
    protected void onSaveState(UIScene scene, SceneState state) {
        // 子类可重写
    }

    /**
     * 恢复状态时的回调
     * 子类可重写以实现自定义恢复逻辑
     *
     * @param scene 场景
     * @param state 状态
     */
    protected void onRestoreState(UIScene scene, SceneState state) {
        // 子类可重写
    }
}
