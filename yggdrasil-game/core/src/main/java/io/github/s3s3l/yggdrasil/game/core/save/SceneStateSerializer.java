package io.github.s3s3l.yggdrasil.game.core.save;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.s3s3l.yggdrasil.game.core.ui.scene.SceneState;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 场景状态序列化器
 * 用于将 SceneState 序列化为 JSON 格式或从 JSON 格式反序列化
 */
@Slf4j
public class SceneStateSerializer {

    private final ObjectMapper objectMapper;

    /**
     * 创建序列化器
     */
    public SceneStateSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 创建序列化器
     *
     * @param objectMapper 自定义 ObjectMapper
     */
    public SceneStateSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 序列化场景状态为 JSON 字符串
     *
     * @param state 场景状态
     * @return JSON 字符串
     */
    public String serialize(SceneState state) {
        if (state == null) {
            return null;
        }

        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("sceneName", state.getSceneName());
            node.put("timestamp", state.getTimestamp());

            ObjectNode dataNode = node.putObject("data");
            for (Map.Entry<String, Object> entry : state.getAll().entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    dataNode.putNull(entry.getKey());
                } else if (value instanceof String) {
                    dataNode.put(entry.getKey(), (String) value);
                } else if (value instanceof Integer) {
                    dataNode.put(entry.getKey(), (Integer) value);
                } else if (value instanceof Float) {
                    dataNode.put(entry.getKey(), (Float) value);
                } else if (value instanceof Double) {
                    dataNode.put(entry.getKey(), (Double) value);
                } else if (value instanceof Boolean) {
                    dataNode.put(entry.getKey(), (Boolean) value);
                } else if (value instanceof Long) {
                    dataNode.put(entry.getKey(), (Long) value);
                } else if (value instanceof Number) {
                    dataNode.put(entry.getKey(), ((Number) value).doubleValue());
                } else {
                    // 其他类型尝试转为字符串
                    dataNode.put(entry.getKey(), value.toString());
                }
            }

            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize scene state", e);
            return null;
        }
    }

    /**
     * 从 JSON 字符串反序列化场景状态
     *
     * @param json JSON 字符串
     * @return 场景状态
     */
    public SceneState deserialize(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            JsonNode node = objectMapper.readTree(json);

            String sceneName = node.get("sceneName").asText();
            long timestamp = node.get("timestamp").asLong();

            SceneState state = new SceneState(sceneName);
            state.setTimestamp(timestamp);

            JsonNode dataNode = node.get("data");
            if (dataNode != null && dataNode.isObject()) {
                dataNode.fields().forEachRemaining(entry -> {
                    String key = entry.getKey();
                    JsonNode valueNode = entry.getValue();

                    if (valueNode.isTextual()) {
                        state.put(key, valueNode.asText());
                    } else if (valueNode.isInt()) {
                        state.put(key, valueNode.asInt());
                    } else if (valueNode.isDouble()) {
                        state.put(key, valueNode.asDouble());
                    } else if (valueNode.isBoolean()) {
                        state.put(key, valueNode.asBoolean());
                    } else if (valueNode.isLong()) {
                        state.put(key, valueNode.asLong());
                    } else if (valueNode.isNumber()) {
                        state.put(key, valueNode.asDouble());
                    } else if (!valueNode.isNull()) {
                        state.put(key, valueNode.asText());
                    }
                });
            }

            return state;
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize scene state from JSON", e);
            return null;
        }
    }

    /**
     * 序列化场景状态到文件
     *
     * @param state 场景状态
     * @param file  文件
     * @return true 如果保存成功
     */
    public boolean saveToFile(SceneState state, File file) {
        String json = serialize(state);
        if (json == null) {
            return false;
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, json);
            return true;
        } catch (IOException e) {
            log.warn("Failed to save scene state to file: {}", file.getName(), e);
            return false;
        }
    }

    /**
     * 从文件加载场景状态
     *
     * @param file 文件
     * @return 场景状态
     */
    public SceneState loadFromFile(File file) {
        if (!file.exists()) {
            return null;
        }

        try {
            String json = objectMapper.readValue(file, String.class);
            return deserialize(json);
        } catch (IOException e) {
            log.warn("Failed to load scene state from file: {}", file.getName(), e);
            return null;
        }
    }

    /**
     * 从文件加载场景状态
     *
     * @param filePath 文件路径
     * @return 场景状态
     */
    public SceneState loadFromFile(String filePath) {
        return loadFromFile(new File(filePath));
    }
}
