package io.github.s3s3l.yggdrasil.game.core.render.profiler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 渲染性能分析器测试
 */
@DisplayName("渲染性能分析器测试")
class RenderProfilerTest {

    private RenderProfiler profiler;

    @BeforeEach
    void setUp() {
        profiler = RenderProfiler.get();
        profiler.setEnabled(true);
        profiler.clearHistory();
        profiler.resetCounters();
    }

    @Test
    @DisplayName("开始和结束帧")
    void testBeginAndEndFrame() {
        profiler.beginFrame();
        assertNotNull(profiler.getCurrentFrame());

        profiler.endFrame();
        assertTrue(profiler.getCurrentFrame().isComplete());
    }

    @Test
    @DisplayName("记录绘制调用")
    void testRecordDrawCalls() {
        profiler.beginFrame();
        profiler.recordDrawCalls(100);
        profiler.endFrame();

        assertEquals(100, profiler.getCurrentFrame().getDrawCalls());
    }

    @Test
    @DisplayName("记录批次数量")
    void testRecordBatches() {
        profiler.beginFrame();
        profiler.recordBatches(10);
        profiler.endFrame();

        assertEquals(10, profiler.getCurrentFrame().getBatchCount());
    }

    @Test
    @DisplayName("记录渲染对象数量")
    void testRecordRenderObjects() {
        profiler.beginFrame();
        profiler.recordRenderObjects(50);
        profiler.endFrame();

        assertEquals(50, profiler.getCurrentFrame().getRenderObjectCount());
    }

    @Test
    @DisplayName("记录剔除对象数量")
    void testRecordCulledObjects() {
        profiler.beginFrame();
        profiler.recordCulledObjects(20);
        profiler.endFrame();

        assertEquals(20, profiler.getCurrentFrame().getCulledObjectCount());
    }

    @Test
    @DisplayName("帧统计历史")
    void testFrameHistory() {
        profiler.clearHistory();

        for (int i = 0; i < 10; i++) {
            profiler.beginFrame();
            profiler.recordFrameTime(16.67f);
            profiler.endFrame();
        }

        List<FrameStats> history = profiler.getFrameHistory();
        assertEquals(10, history.size());
    }

    @Test
    @DisplayName("平均帧时间")
    void testAverageFrameTime() {
        profiler.clearHistory();

        for (int i = 0; i < 5; i++) {
            profiler.beginFrame();
            profiler.recordFrameTime(16.67f);
            profiler.endFrame();
        }

        assertTrue(profiler.getAverageFrameTime() > 0);
    }

    @Test
    @DisplayName("平均 FPS")
    void testAverageFPS() {
        profiler.clearHistory();

        for (int i = 0; i < 5; i++) {
            profiler.beginFrame();
            profiler.recordFrameTime(16.67f);  // ~60 FPS
            profiler.endFrame();
        }

        float avgFPS = profiler.getAverageFPS();
        assertTrue(avgFPS > 50);  // 应该接近 60
        assertTrue(avgFPS < 70);
    }

    @Test
    @DisplayName("计数器操作")
    void testCounterOperations() {
        profiler.incrementDrawCall();
        profiler.incrementDrawCall();
        profiler.incrementDrawCall();

        assertEquals(3, profiler.getCounter("drawCalls").get());

        profiler.resetCounter("drawCalls");
        assertEquals(0, profiler.getCounter("drawCalls").get());
    }

    @Test
    @DisplayName("禁用分析器")
    void testDisableProfiler() {
        profiler.setEnabled(false);

        profiler.beginFrame();
        profiler.recordDrawCalls(100);
        profiler.endFrame();

        // 禁用后，帧统计应该为空
        assertTrue(profiler.getFrameHistory().isEmpty());
    }

    @Test
    @DisplayName("启用分析器")
    void testEnableProfiler() {
        profiler.setEnabled(false);
        profiler.setEnabled(true);

        profiler.beginFrame();
        profiler.recordDrawCalls(100);
        profiler.endFrame();

        assertEquals(1, profiler.getFrameHistory().size());
    }

    @Test
    @DisplayName("清除帧历史")
    void testClearHistory() {
        for (int i = 0; i < 5; i++) {
            profiler.beginFrame();
            profiler.endFrame();
        }

        profiler.clearHistory();
        assertTrue(profiler.getFrameHistory().isEmpty());
    }

    @Test
    @DisplayName("帧统计 toString")
    void testFrameStatsToString() {
        profiler.beginFrame();
        profiler.recordFrameTime(16.67f);
        profiler.recordDrawCalls(100);
        profiler.endFrame();

        String statsString = profiler.getCurrentFrame().toString();
        assertNotNull(statsString);
        assertTrue(statsString.contains("FrameStats"));
    }

    @Test
    @DisplayName("获取最大帧历史数")
    void testGetMaxFrameHistory() {
        assertTrue(profiler.getMaxFrameHistory() > 0);
    }
}
