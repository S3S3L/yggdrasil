package io.github.s3s3l.yggdrasil.game.core.render.profiler;

/**
 * 帧统计信息
 */
public class FrameStats {

    /**
     * 帧开始时间（纳秒）
     */
    private long startTime;

    /**
     * 帧结束时间（纳秒）
     */
    private long endTime;

    /**
     * 帧时间（毫秒）
     */
    private float frameTime;

    /**
     * 绘制调用次数
     */
    private int drawCalls;

    /**
     * 批次数量
     */
    private int batchCount;

    /**
     * 渲染对象数量
     */
    private int renderObjectCount;

    /**
     * 剔除对象数量
     */
    private int culledObjectCount;

    /**
     * 是否已完成
     */
    private boolean complete;

    /**
     * 创建帧统计
     */
    public FrameStats() {
        this.startTime = System.nanoTime();
        this.complete = false;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间（纳秒）
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间（纳秒）
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
        // Only calculate frame time if not already set
        if (this.frameTime <= 0) {
            this.frameTime = (endTime - startTime) / 1_000_000f;
        }
    }

    /**
     * 设置帧时间
     *
     * @param frameTime 帧时间（毫秒）
     */
    public void setFrameTime(float frameTime) {
        this.frameTime = frameTime;
    }

    /**
     * 设置绘制调用次数
     *
     * @param drawCalls 绘制调用次数
     */
    public void setDrawCalls(int drawCalls) {
        this.drawCalls = drawCalls;
    }

    /**
     * 设置批次数量
     *
     * @param batchCount 批次数量
     */
    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    /**
     * 设置渲染对象数量
     *
     * @param renderObjectCount 渲染对象数量
     */
    public void setRenderObjectCount(int renderObjectCount) {
        this.renderObjectCount = renderObjectCount;
    }

    /**
     * 设置剔除对象数量
     *
     * @param culledObjectCount 剔除对象数量
     */
    public void setCulledObjectCount(int culledObjectCount) {
        this.culledObjectCount = culledObjectCount;
    }

    /**
     * 设置完成状态
     *
     * @param complete true 如果已完成
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * 获取帧开始时间
     *
     * @return 开始时间（纳秒）
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 获取帧结束时间
     *
     * @return 结束时间（纳秒）
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * 获取帧时间
     *
     * @return 帧时间（毫秒）
     */
    public float getFrameTime() {
        return frameTime;
    }

    /**
     * 获取绘制调用次数
     *
     * @return 绘制调用次数
     */
    public int getDrawCalls() {
        return drawCalls;
    }

    /**
     * 获取批次数量
     *
     * @return 批次数量
     */
    public int getBatchCount() {
        return batchCount;
    }

    /**
     * 获取渲染对象数量
     *
     * @return 渲染对象数量
     */
    public int getRenderObjectCount() {
        return renderObjectCount;
    }

    /**
     * 获取剔除对象数量
     *
     * @return 剔除对象数量
     */
    public int getCulledObjectCount() {
        return culledObjectCount;
    }

    /**
     * 检查是否已完成
     *
     * @return true 如果已完成
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * 获取 FPS
     *
     * @return FPS
     */
    public float getFPS() {
        return frameTime > 0 ? 1000f / frameTime : 0;
    }

    @Override
    public String toString() {
        return String.format("FrameStats[time=%.2fms, FPS=%.1f, drawCalls=%d, batches=%d, objects=%d]",
                frameTime, getFPS(), drawCalls, batchCount, renderObjectCount);
    }
}
