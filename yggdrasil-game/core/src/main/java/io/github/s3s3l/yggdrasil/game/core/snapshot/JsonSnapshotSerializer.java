package io.github.s3s3l.yggdrasil.game.core.snapshot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 基于 JSON 的快照序列化器
 * 使用 Jackson 库进行序列化和反序列化
 */
public class JsonSnapshotSerializer implements SnapshotSerializer {

    private final ObjectMapper objectMapper;

    public JsonSnapshotSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonSnapshotSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(Snapshot snapshot) {
        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("snapshotType", snapshot.type().name());
            node.put("id", snapshot.getId());

            if (snapshot instanceof ObjectSnapshot) {
                ObjectSnapshot objSnapshot = (ObjectSnapshot) snapshot;
                node.put("type", objSnapshot.getType().getName());

                // 序列化属性
                ObjectNode propsNode = node.putObject("props");
                objSnapshot.getProps().forEach((k, v) -> propsNode.put((String) k, (String) v));

                // 序列化状态
                ObjectNode statesNode = node.putObject("states");
                objSnapshot.getStates().forEach((name, state) -> {
                    ObjectNode stateNode = statesNode.putObject(name);
                    stateNode.put("type", state.getType().getName());
                    stateNode.set("data", state.getData());
                });
            }

            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize snapshot", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Snapshot> T deserialize(String data, Class<T> targetType) {
        try {
            JsonNode node = objectMapper.readTree(data);

            long id = node.get("id").asLong();
            String typeName = node.get("type").asText();
            Class<?> typeClass = Class.forName(typeName);

            ObjectSnapshot snapshot = new ObjectSnapshot(id, (Class<? extends io.github.s3s3l.yggdrasil.game.core.basic.GameAtom>) typeClass);

            // 反序列化属性
            JsonNode propsNode = node.get("props");
            if (propsNode != null && propsNode.isObject()) {
                propsNode.fields().forEachRemaining(entry -> {
                    snapshot.addProps(entry.getKey(), entry.getValue().asText());
                });
            }

            // 反序列化状态
            JsonNode statesNode = node.get("states");
            if (statesNode != null && statesNode.isObject()) {
                statesNode.fields().forEachRemaining(entry -> {
                    String stateName = entry.getKey();
                    JsonNode stateNode = entry.getValue();
                    String stateType = stateNode.get("type").asText();
                    JsonNode stateData = stateNode.get("data");

                    try {
                        snapshot.addState(stateName, new StateSnapshot(
                                stateData,
                                Class.forName(stateType)
                        ));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Failed to deserialize state type: " + stateType, e);
                    }
                });
            }

            return (T) snapshot;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize snapshot", e);
        }
    }
}
