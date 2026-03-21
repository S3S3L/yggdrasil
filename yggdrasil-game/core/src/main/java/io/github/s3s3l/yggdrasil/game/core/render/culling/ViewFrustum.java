package io.github.s3s3l.yggdrasil.game.core.render.culling;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;

/**
 * 视锥（视野范围）
 * 用于视锥剔除，只渲染视野范围内的对象
 */
public class ViewFrustum {

    /**
     * 视锥中心位置
     */
    private final Vector2 center;

    /**
     * 视锥宽度
     */
    private float width;

    /**
     * 视锥高度
     */
    private float height;

    /**
     * 视锥左边界
     */
    private float left;

    /**
     * 视锥右边界
     */
    private float right;

    /**
     * 视锥下边界
     */
    private float bottom;

    /**
     * 视锥上边界
     */
    private float top;

    /**
     * 创建视锥
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param width   宽度
     * @param height  高度
     */
    public ViewFrustum(float centerX, float centerY, float width, float height) {
        this.center = new Vector2(centerX, centerY);
        setSize(width, height);
    }

    /**
     * 创建视锥
     *
     * @param center 中心位置
     * @param width  宽度
     * @param height 高度
     */
    public ViewFrustum(Vector2 center, float width, float height) {
        this.center = new Vector2(center);
        setSize(width, height);
    }

    /**
     * 设置视锥大小
     *
     * @param width  宽度
     * @param height 高度
     */
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateBounds();
    }

    /**
     * 设置视锥中心
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void setCenter(float x, float y) {
        this.center.x = x;
        this.center.y = y;
        updateBounds();
    }

    /**
     * 设置视锥中心
     *
     * @param center 中心位置
     */
    public void setCenter(Vector2 center) {
        this.center.set(center);
        updateBounds();
    }

    /**
     * 更新边界
     */
    private void updateBounds() {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        this.left = center.x - halfWidth;
        this.right = center.x + halfWidth;
        this.bottom = center.y - halfHeight;
        this.top = center.y + halfHeight;
    }

    /**
     * 检查点是否在视锥内
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return true 如果在视锥内
     */
    public boolean contains(float x, float y) {
        return x >= left && x <= right && y >= bottom && y <= top;
    }

    /**
     * 检查点是否在视锥内
     *
     * @param point 点位置
     * @return true 如果在视锥内
     */
    public boolean contains(Vector2 point) {
        return contains(point.x, point.y);
    }

    /**
     * 检查矩形是否在视锥内或与视锥相交
     *
     * @param x      矩形 X 坐标
     * @param y      矩形 Y 坐标
     * @param width  矩形宽度
     * @param height 矩形高度
     * @return true 如果在视锥内或与视锥相交
     */
    public boolean intersects(float x, float y, float width, float height) {
        float halfW = width / 2f;
        float halfH = height / 2f;
        float left2 = x - halfW;
        float right2 = x + halfW;
        float bottom2 = y - halfH;
        float top2 = y + halfH;

        // 检查两个矩形是否相交
        return !(left2 > this.right || right2 < this.left ||
                bottom2 > this.top || top2 < this.bottom);
    }

    /**
     * 检查矩形是否完全在视锥内
     *
     * @param x      矩形 X 坐标
     * @param y      矩形 Y 坐标
     * @param width  矩形宽度
     * @param height 矩形高度
     * @return true 如果完全在视锥内
     */
    public boolean containsRect(float x, float y, float width, float height) {
        float halfW = width / 2f;
        float halfH = height / 2f;
        float left2 = x - halfW;
        float right2 = x + halfW;
        float bottom2 = y - halfH;
        float top2 = y + halfH;

        // 检查矩形是否完全在视锥内
        return left2 >= this.left && right2 <= this.right &&
                bottom2 >= this.bottom && top2 <= this.top;
    }

    /**
     * 获取视锥中心
     *
     * @return 中心位置
     */
    public Vector2 getCenter() {
        return new Vector2(center);
    }

    /**
     * 获取视锥宽度
     *
     * @return 宽度
     */
    public float getWidth() {
        return width;
    }

    /**
     * 获取视锥高度
     *
     * @return 高度
     */
    public float getHeight() {
        return height;
    }

    /**
     * 获取左边界
     *
     * @return 左边界
     */
    public float getLeft() {
        return left;
    }

    /**
     * 获取右边界
     *
     * @return 右边界
     */
    public float getRight() {
        return right;
    }

    /**
     * 获取下边界
     *
     * @return 下边界
     */
    public float getBottom() {
        return bottom;
    }

    /**
     * 获取上边界
     *
     * @return 上边界
     */
    public float getTop() {
        return top;
    }

    /**
     * 扩展视锥以包含指定点
     *
     * @param point 点位置
     */
    public void expandToPoint(Vector2 point) {
        expandToPoint(point.x, point.y);
    }

    /**
     * 扩展视锥以包含指定点
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void expandToPoint(float x, float y) {
        if (x < left) left = x;
        if (x > right) right = x;
        if (y < bottom) bottom = y;
        if (y > top) top = y;

        // 重新计算中心和大小
        this.center.x = (left + right) / 2f;
        this.center.y = (bottom + top) / 2f;
        this.width = right - left;
        this.height = top - bottom;
    }

    @Override
    public String toString() {
        return String.format("ViewFrustum[center=(%.2f, %.2f), size=%.2fx%.2f]",
                center.x, center.y, width, height);
    }
}
