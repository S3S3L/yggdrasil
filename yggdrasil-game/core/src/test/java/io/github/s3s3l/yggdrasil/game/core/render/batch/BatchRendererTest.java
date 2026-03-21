package io.github.s3s3l.yggdrasil.game.core.render.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 批次渲染器测试
 */
@DisplayName("批次渲染器测试")
class BatchRendererTest {

    private BatchRenderer<TestRenderData> renderer;

    @BeforeEach
    void setUp() {
        renderer = new BatchRenderer<>(10);
    }

    @Test
    @DisplayName("开始和结束批次")
    void testBeginAndEnd() {
        renderer.begin();
        assertTrue(renderer.isBuilding());

        List<RenderBatch<TestRenderData>> batches = renderer.end();
        assertFalse(renderer.isBuilding());
        assertTrue(batches.isEmpty());
    }

    @Test
    @DisplayName("添加渲染数据")
    void testAddRenderData() {
        renderer.begin();

        TestRenderData data1 = new TestRenderData("texture1", 1);
        TestRenderData data2 = new TestRenderData("texture1", 2);
        renderer.add(data1);
        renderer.add(data2);

        List<RenderBatch<TestRenderData>> batches = renderer.end();

        assertEquals(1, batches.size());
        assertEquals(2, batches.get(0).getSize());
    }

    @Test
    @DisplayName("不同批次键创建不同批次")
    void testDifferentBatchKeys() {
        renderer.begin();

        renderer.add(new TestRenderData("texture1", 1));
        renderer.add(new TestRenderData("texture2", 2));
        renderer.add(new TestRenderData("texture1", 3));

        List<RenderBatch<TestRenderData>> batches = renderer.end();

        // 应该有 2 个批次（texture1 和 texture2）
        assertEquals(2, batches.size());
    }

    @Test
    @DisplayName("批次大小限制")
    void testBatchSizeLimit() {
        renderer = new BatchRenderer<>(5);
        renderer.begin();

        // 添加 12 个数据，应该创建 3 个批次
        for (int i = 0; i < 12; i++) {
            renderer.add(new TestRenderData("texture1", i));
        }

        List<RenderBatch<TestRenderData>> batches = renderer.end();

        assertTrue(batches.size() >= 2);  // 至少 2 个批次
    }

    @Test
    @DisplayName("未开始就添加数据抛出异常")
    void testAddWithoutBegin() {
        assertThrows(IllegalStateException.class, () -> {
            renderer.add(new TestRenderData("texture1", 1));
        });
    }

    @Test
    @DisplayName("未结束就获取批次抛出异常")
    void testEndWithoutBegin() {
        assertThrows(IllegalStateException.class, () -> {
            renderer.end();
        });
    }

    @Test
    @DisplayName("清空批次")
    void testClear() {
        renderer.begin();
        renderer.add(new TestRenderData("texture1", 1));
        renderer.clear();

        assertTrue(renderer.getBatches().isEmpty());
        assertFalse(renderer.isBuilding());
    }

    @Test
    @DisplayName("统计信息")
    void testStats() {
        renderer.begin();
        renderer.add(new TestRenderData("texture1", 1));
        renderer.add(new TestRenderData("texture1", 2));
        renderer.end();

        BatchStats stats = renderer.getStats();
        assertEquals(1, stats.getBeginCount());
        assertEquals(1, stats.getEndCount());
        assertEquals(2, stats.getAddCount());
    }

    @Test
    @DisplayName("批次合并率")
    void testBatchMergeRatio() {
        renderer.begin();

        // 添加 10 个相同批次的数据
        for (int i = 0; i < 10; i++) {
            renderer.add(new TestRenderData("texture1", i));
        }

        renderer.end();

        BatchStats stats = renderer.getStats();
        assertTrue(stats.getBatchMergeRatio() > 1);  // 应该有合并效果
    }

    /**
     * 测试用渲染数据
     */
    static class TestRenderData implements BatchRenderData {
        private final String textureId;
        private final int renderOrder;

        TestRenderData(String textureId, int renderOrder) {
            this.textureId = textureId;
            this.renderOrder = renderOrder;
        }

        @Override
        public String getBatchKey() {
            return textureId;
        }

        @Override
        public int getRenderOrder() {
            return renderOrder;
        }
    }
}
