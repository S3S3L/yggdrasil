package io.github.s3s3l.yggdrasil.game.core.render.profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 渲染性能分析器
 * 统计渲染性能相关指标
 */
public class RenderProfiler {

    /**
     * 单例实例
     */
    private static final RenderProfiler INSTANCE = new RenderProfiler();

    /**
     * 帧统计列表（保留最近的帧）
     */
    private final List<FrameStats> frameStats;

    /**
     * 最大保留帧数
     */
    private final int maxFrameHistory;

    /**
     * 当前帧统计
     */
    private FrameStats currentFrame;

    /**
     * 是否启用分析
     */
    private boolean enabled;

    /**
     * 渲染计数器
     */
    private final Map<String, AtomicLong> counters;

    /**
     * 私有构造函数（单例）
     */
    private RenderProfiler() {
        this.maxFrameHistory = 60;
        this.frameStats = new ArrayList<>(maxFrameHistory);
        this.enabled = true;
        this.counters = new ConcurrentHashMap<>();
        this.currentFrame = new FrameStats();
    }

    /**
     * 获取渲染性能分析器实例
     *
     * @return 单例实例
     */
    public static RenderProfiler get() {
        return INSTANCE;
    }

    /**
     * 开始新帧
     */
    public void beginFrame() {
        if (!enabled) {
            return;
        }

        // Create new frame for this begin
        currentFrame = new FrameStats();
        currentFrame.setStartTime(System.nanoTime());
    }

    /**
     * 结束当前帧
     */
    public void endFrame() {
        if (!enabled || currentFrame == null) {
            return;
        }

        currentFrame.setEndTime(System.nanoTime());
        currentFrame.setComplete(true);
        addFrameStats(currentFrame);
    }

    /**
     * 添加帧统计
     *
     * @param stats 帧统计
     */
    private synchronized void addFrameStats(FrameStats stats) {
        frameStats.add(stats);

        // 移除超出的帧
        while (frameStats.size() > maxFrameHistory) {
            frameStats.remove(0);
        }
    }

    /**
     * 记录绘制调用
     *
     * @param count 绘制调用次数
     */
    public void recordDrawCalls(int count) {
        if (!enabled || currentFrame == null) {
            return;
        }
        currentFrame.setDrawCalls(count);
    }

    /**
     * 增加绘制调用计数
     *
     * @return 新的计数
     */
    public long incrementDrawCall() {
        return getCounter("drawCalls").incrementAndGet();
    }

    /**
     * 记录批次数量
     *
     * @param count 批次数量
     */
    public void recordBatches(int count) {
        if (!enabled || currentFrame == null) {
            return;
        }
        currentFrame.setBatchCount(count);
    }

    /**
     * 记录渲染对象数量
     *
     * @param count 对象数量
     */
    public void recordRenderObjects(int count) {
        if (!enabled || currentFrame == null) {
            return;
        }
        currentFrame.setRenderObjectCount(count);
    }

    /**
     * 记录剔除对象数量
     *
     * @param count 剔除数量
     */
    public void recordCulledObjects(int count) {
        if (!enabled || currentFrame == null) {
            return;
        }
        currentFrame.setCulledObjectCount(count);
    }

    /**
     * 记录帧时间
     *
     * @param timeMs 帧时间（毫秒）
     */
    public void recordFrameTime(float timeMs) {
        if (!enabled || currentFrame == null) {
            return;
        }
        currentFrame.setFrameTime(timeMs);
    }

    /**
     * 获取计数器
     *
     * @param name 计数器名称
     * @return 计数器
     */
    public AtomicLong getCounter(String name) {
        return counters.computeIfAbsent(name, k -> new AtomicLong(0));
    }

    /**
     * 重置计数器
     *
     * @param name 计数器名称
     */
    public void resetCounter(String name) {
        AtomicLong counter = counters.get(name);
        if (counter != null) {
            counter.set(0);
        }
    }

    /**
     * 重置所有计数器
     */
    public void resetCounters() {
        counters.clear();
    }

    /**
     * 获取平均帧时间
     *
     * @return 平均帧时间（毫秒）
     */
    public synchronized float getAverageFrameTime() {
        if (frameStats.isEmpty()) {
            return 0;
        }

        float total = 0;
        for (FrameStats stats : frameStats) {
            total += stats.getFrameTime();
        }
        return total / frameStats.size();
    }

    /**
     * 获取平均 FPS
     *
     * @return 平均 FPS
     */
    public synchronized float getAverageFPS() {
        float avgFrameTime = getAverageFrameTime();
        return avgFrameTime > 0 ? 1000f / avgFrameTime : 0;
    }

    /**
     * 获取平均绘制调用次数
     *
     * @return 平均绘制调用次数
     */
    public synchronized int getAverageDrawCalls() {
        if (frameStats.isEmpty()) {
            return 0;
        }

        int total = 0;
        for (FrameStats stats : frameStats) {
            total += stats.getDrawCalls();
        }
        return total / frameStats.size();
    }

    /**
     * 获取平均批次数量
     *
     * @return 平均批次数量
     */
    public synchronized int getAverageBatches() {
        if (frameStats.isEmpty()) {
            return 0;
        }

        int total = 0;
        for (FrameStats stats : frameStats) {
            total += stats.getBatchCount();
        }
        return total / frameStats.size();
    }

    /**
     * 获取帧统计历史
     *
     * @return 帧统计列表
     */
    public synchronized List<FrameStats> getFrameHistory() {
        return new ArrayList<>(frameStats);
    }

    /**
     * 获取当前帧统计
     *
     * @return 当前帧统计
     */
    public FrameStats getCurrentFrame() {
        return currentFrame;
    }

    /**
     * 是否启用分析
     *
     * @return true 如果启用
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置是否启用分析
     *
     * @param enabled true 启用
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            frameStats.clear();
            currentFrame = null;
        } else {
            currentFrame = new FrameStats();
        }
    }

    /**
     * 清除帧统计历史
     */
    public synchronized void clearHistory() {
        frameStats.clear();
        currentFrame = new FrameStats();
    }

    /**
     * 获取最大帧历史数
     *
     * @return 最大帧历史数
     */
    public int getMaxFrameHistory() {
        return maxFrameHistory;
    }
}
