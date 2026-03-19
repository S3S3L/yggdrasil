package io.github.s3s3l.yggdrasil.game.core.snapshot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JsonSnapshotSerializer 单元测试
 */
class JsonSnapshotSerializerTest {

    private JsonSnapshotSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new JsonSnapshotSerializer();
    }

    @Test
    void testSerializeAndDeserialize() {
        // 创建测试快照
        long testId = 123L;
        ObjectSnapshot snapshot = new ObjectSnapshot(testId, TestGameAtom.class);
        snapshot.addProps("name", "TestObject");
        snapshot.addProps("count", "42");

        // 序列化
        String json = serializer.serialize(snapshot);

        // 验证 JSON 不为空
        assertNotNull(json);
        assertTrue(json.contains("\"id\":123"));
        assertTrue(json.contains("TestObject"));

        // 反序列化
        ObjectSnapshot deserialized = serializer.deserialize(json, ObjectSnapshot.class);

        // 验证反序列化后的内容
        assertNotNull(deserialized);
        assertEquals(testId, deserialized.getId());
        assertEquals("TestObject", deserialized.getProps().getProperty("name"));
        assertEquals("42", deserialized.getProps().getProperty("count"));
    }

    @Test
    void testSerializeWithStates() {
        // TODO: 添加状态序列化的测试
        // 这需要完善 StateSnapshot 的序列化支持
        assertDoesNotThrow(() -> {
            ObjectSnapshot snapshot = new ObjectSnapshot(1L, TestGameAtom.class);
            serializer.serialize(snapshot);
        });
    }

    @Test
    void testDeserializeInvalidJson() {
        // 测试无效 JSON 处理
        assertThrows(RuntimeException.class, () -> {
            serializer.deserialize("invalid json", ObjectSnapshot.class);
        });
    }

    @Test
    void testDeserializeNonExistentClass() {
        // 创建包含不存在类的 JSON
        String json = "{\"id\":1,\"type\":\"com.example.NonExistentClass\",\"props\":{}}";

        // 应该抛出异常
        assertThrows(RuntimeException.class, () -> {
            serializer.deserialize(json, ObjectSnapshot.class);
        });
    }

    /**
     * 测试用的 GameAtom 实现
     */
    static class TestGameAtom implements io.github.s3s3l.yggdrasil.game.core.basic.GameAtom {
        private long id = 1L;

        @Override
        public void afterPropertiesSet() {}

        @Override
        public long getId() {
            return id;
        }

        @Override
        public void setId(long id) {
            this.id = id;
        }

        @Override
        public void onWindowResize(int width, int height) {}

        @Override
        public void calculate(float delta) {}

        @Override
        public void preRender() {}

        @Override
        public void render() {}

        @Override
        public boolean isDirty() {
            return false;
        }

        @Override
        public void markDirty() {}

        @Override
        public void clearDirty() {}

        @Override
        public io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot snapshot() {
            return null;
        }

        @Override
        public void restore(io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot snapshot) {}
    }
}
