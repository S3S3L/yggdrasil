package io.github.s3s3l.yggdrasil.game.core.render.culling;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 剔除统计信息
 */
public class CullStats {

    /**
     * 总剔除次数
     */
    private final AtomicLong cullCount = new AtomicLong(0);

    /**
     * 总剔除对象数量
     */
    private final AtomicLong culledObjects = new AtomicLong(0);

    /**
     * 总检查对象数量
     */
    private final AtomicLong checkedObjects = new AtomicLong(0);

    /**
     * 记录剔除操作
     *
     * @param checked 检查的对象数量
     * @param culled  剔除的对象数量
     */
    void recordCull(int checked, int culled) {
        cullCount.incrementAndGet();
        checkedObjects.addAndGet(checked);
        culledObjects.addAndGet(culled);
    }

    /**
     * 获取总剔除次数
     *
     * @return 剔除次数
     */
    public long getCullCount() {
        return cullCount.get();
    }

    /**
     * 获取总剔除对象数量
     *
     * @return 剔除对象数量
     */
    public long getCulledObjects() {
        return culledObjects.get();
    }

    /**
     * 获取总检查对象数量
     *
     * @return 检查对象数量
     */
    public long getCheckedObjects() {
        return checkedObjects.get();
    }

    /**
     * 获取剔除率（剔除对象数/检查对象数）
     *
     * @return 剔除率
     */
    public float getCullRate() {
        long checked = checkedObjects.get();
        return checked > 0 ? (float) culledObjects.get() / checked : 0;
    }

    /**
     * 获取可见率（可见对象数/检查对象数）
     *
     * @return 可见率
     */
    public float getVisibleRate() {
        long checked = checkedObjects.get();
        long culled = culledObjects.get();
        return checked > 0 ? 1.0f - ((float) culled / checked) : 1.0f;
    }

    @Override
    public String toString() {
        return String.format("CullStats[checked=%d, culled=%d, rate=%.2f%%]",
                checkedObjects.get(), culledObjects.get(), getCullRate() * 100);
    }
}
