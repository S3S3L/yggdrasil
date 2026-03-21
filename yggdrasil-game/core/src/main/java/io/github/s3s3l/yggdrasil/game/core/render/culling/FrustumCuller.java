package io.github.s3s3l.yggdrasil.game.core.render.culling;

import java.util.ArrayList;
import java.util.List;

/**
 * 视锥剔除器
 * 用于剔除视锥外的对象，减少渲染负担
 */
public class FrustumCuller {

    /**
     * 视锥
     */
    private final ViewFrustum frustum;

    /**
     * 是否启用剔除
     */
    private boolean enabled;

    /**
     * 剔除统计信息
     */
    private CullStats stats;

    /**
     * 创建视锥剔除器
     *
     * @param frustum 视锥
     */
    public FrustumCuller(ViewFrustum frustum) {
        this.frustum = frustum;
        this.enabled = true;
        this.stats = new CullStats();
    }

    /**
     * 创建视锥剔除器
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param width   宽度
     * @param height  高度
     */
    public FrustumCuller(float centerX, float centerY, float width, float height) {
        this(new ViewFrustum(centerX, centerY, width, height));
    }

    /**
     * 剔除对象列表
     *
     * @param objects 待剔除的对象列表
     * @return 可见的对象列表
     */
    public <T extends Cullable> List<T> cull(List<T> objects) {
        if (!enabled) {
            stats.recordCull(objects.size(), 0);
            return new ArrayList<>(objects);
        }

        List<T> visible = new ArrayList<>(objects.size());

        for (T object : objects) {
            if (isVisible(object)) {
                visible.add(object);
            }
        }

        int culled = objects.size() - visible.size();
        stats.recordCull(objects.size(), culled);
        return visible;
    }

    /**
     * 检查单个对象是否可见
     *
     * @param object 待检查的对象
     * @return true 如果对象可见
     */
    public <T extends Cullable> boolean isVisible(T object) {
        if (object == null) {
            return false;
        }

        if (!object.isVisible()) {
            return false;
        }

        float centerX = object.getCenterX();
        float centerY = object.getCenterY();
        float width = object.getBoundingWidth();
        float height = object.getBoundingHeight();

        return frustum.intersects(centerX, centerY, width, height);
    }

    /**
     * 检查点是否可见
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return true 如果点可见
     */
    public boolean isVisible(float x, float y) {
        return frustum.contains(x, y);
    }

    /**
     * 检查矩形是否可见
     *
     * @param x      矩形 X 坐标
     * @param y      矩形 Y 坐标
     * @param width  矩形宽度
     * @param height 矩形高度
     * @return true 如果矩形可见（在视锥内或与视锥相交）
     */
    public boolean isVisible(float x, float y, float width, float height) {
        return frustum.intersects(x, y, width, height);
    }

    /**
     * 更新视锥
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param width   宽度
     * @param height  高度
     */
    public void updateFrustum(float centerX, float centerY, float width, float height) {
        frustum.setCenter(centerX, centerY);
        frustum.setSize(width, height);
    }

    /**
     * 更新视锥中心
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void updateFrustumCenter(float x, float y) {
        frustum.setCenter(x, y);
    }

    /**
     * 更新视锥大小
     *
     * @param width  宽度
     * @param height 高度
     */
    public void updateFrustumSize(float width, float height) {
        frustum.setSize(width, height);
    }

    /**
     * 获取视锥
     *
     * @return 视锥
     */
    public ViewFrustum getFrustum() {
        return frustum;
    }

    /**
     * 是否启用剔除
     *
     * @return true 如果启用
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置是否启用剔除
     *
     * @param enabled true 启用
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取剔除统计信息
     *
     * @return 统计信息
     */
    public CullStats getStats() {
        return stats;
    }

    /**
     * 重置统计信息
     */
    public void resetStats() {
        this.stats = new CullStats();
    }
}
