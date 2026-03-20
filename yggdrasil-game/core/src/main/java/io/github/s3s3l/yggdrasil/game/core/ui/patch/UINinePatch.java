package io.github.s3s3l.yggdrasil.game.core.ui.patch;

/**
 * NinePatch 图片类
 * 支持 9 宫格缩放的图片
 *
 * 9 宫格将图片分为 9 个区域：
 * - 四个角：不缩放
 * - 四个边：单方向缩放
 * - 中间：双向缩放
 */
public class UINinePatch {

    private final float[] patches; // left, top, right, bottom 定义可拉伸区域
    private float width;
    private float height;

    /**
     * 创建 NinePatch
     *
     * @param width  原始宽度
     * @param height 原始高度
     * @param left   左边可拉伸区域起始位置
     * @param top    顶部可拉伸区域起始位置
     * @param right  右边可拉伸区域结束位置
     * @param bottom 底部可拉伸区域结束位置
     */
    public UINinePatch(float width, float height, float left, float top, float right, float bottom) {
        this.width = width;
        this.height = height;
        this.patches = new float[]{left, top, right, bottom};
    }

    /**
     * 创建 NinePatch（对称）
     *
     * @param width   原始宽度
     * @param height  原始高度
     * @param padding 四边可拉伸区域内边距
     */
    public UINinePatch(float width, float height, float padding) {
        this(width, height, padding, padding, width - padding, height - padding);
    }

    /**
     * 获取原始宽度
     *
     * @return 原始宽度
     */
    public float getOriginalWidth() {
        return width;
    }

    /**
     * 获取原始高度
     *
     * @return 原始高度
     */
    public float getOriginalHeight() {
        return height;
    }

    /**
     * 获取左边可拉伸区域起始位置
     *
     * @return left 值
     */
    public float getLeft() {
        return patches[0];
    }

    /**
     * 获取顶部可拉伸区域起始位置
     *
     * @return top 值
     */
    public float getTop() {
        return patches[1];
    }

    /**
     * 获取右边可拉伸区域结束位置
     *
     * @return right 值
     */
    public float getRight() {
        return patches[2];
    }

    /**
     * 获取底部可拉伸区域结束位置
     *
     * @return bottom 值
     */
    public float getBottom() {
        return patches[3];
    }

    /**
     * 计算绘制宽度
     *
     * @param targetWidth 目标宽度
     * @return 实际绘制宽度
     */
    public float getDrawWidth(float targetWidth) {
        return targetWidth;
    }

    /**
     * 计算绘制高度
     *
     * @param targetHeight 目标高度
     * @return 实际绘制高度
     */
    public float getDrawHeight(float targetHeight) {
        return targetHeight;
    }

    /**
     * 获取九个区域的绘制信息
     * 返回格式：[区域索引][x, y, width, height, srcX, srcY, srcWidth, srcHeight]
     *
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @param x            绘制起始 X 坐标
     * @param y            绘制起始 Y 坐标
     * @return 九个区域的绘制信息数组
     */
    public float[][] getDrawRegions(float targetWidth, float targetHeight, float x, float y) {
        float[][] regions = new float[9][8];

        float left = patches[0];
        float top = patches[1];
        float right = patches[2];
        float bottom = patches[3];

        // 计算固定区域大小
        float leftWidth = left;
        float rightWidth = width - right;
        float topHeight = top;
        float bottomHeight = height - bottom;

        // 计算可拉伸区域大小
        float centerWidth = targetWidth - leftWidth - rightWidth;
        float centerHeight = targetHeight - topHeight - bottomHeight;

        // 区域索引：
        // 0 1 2
        // 3 4 5
        // 6 7 8

        float drawX = x;
        float drawY = y;

        // 区域 0 - 左上角
        regions[0] = new float[]{drawX, drawY, leftWidth, topHeight, 0, 0, left, top};

        // 区域 1 - 上边
        regions[1] = new float[]{
            drawX + leftWidth, drawY, centerWidth, topHeight,
            left, 0, right - left, top
        };

        // 区域 2 - 右上角
        regions[2] = new float[]{
            drawX + leftWidth + centerWidth, drawY, rightWidth, topHeight,
            right, 0, width - right, top
        };

        // 区域 3 - 左边
        regions[3] = new float[]{
            drawX, drawY + topHeight, leftWidth, centerHeight,
            0, top, left, bottom - top
        };

        // 区域 4 - 中间（可拉伸）
        regions[4] = new float[]{
            drawX + leftWidth, drawY + topHeight, centerWidth, centerHeight,
            left, top, right - left, bottom - top
        };

        // 区域 5 - 右边
        regions[5] = new float[]{
            drawX + leftWidth + centerWidth, drawY + topHeight, rightWidth, centerHeight,
            right, top, width - right, bottom - top
        };

        // 区域 6 - 左下角
        regions[6] = new float[]{
            drawX, drawY + topHeight + centerHeight, leftWidth, bottomHeight,
            0, bottom, left, height - bottom
        };

        // 区域 7 - 下边
        regions[7] = new float[]{
            drawX + leftWidth, drawY + topHeight + centerHeight, centerWidth, bottomHeight,
            left, bottom, right - left, height - bottom
        };

        // 区域 8 - 右下角
        regions[8] = new float[]{
            drawX + leftWidth + centerWidth, drawY + topHeight + centerHeight, rightWidth, bottomHeight,
            right, bottom, width - right, height - bottom
        };

        return regions;
    }

    /**
     * 检查点是否在可拉伸区域内
     *
     * @param regionIndex 区域索引 (0-8)
     * @param x           x 坐标（相对于 NinePatch 左上角）
     * @param y           y 坐标（相对于 NinePatch 左上角）
     * @return true 如果点在指定区域内
     */
    public boolean isPointInRegion(int regionIndex, float x, float y) {
        float left = patches[0];
        float top = patches[1];
        float right = patches[2];
        float bottom = patches[3];

        switch (regionIndex) {
            case 0: // 左上角
                return x < left && y < top;
            case 1: // 上边
                return x >= left && x <= right && y < top;
            case 2: // 右上角
                return x > right && y < top;
            case 3: // 左边
                return x < left && y >= top && y <= bottom;
            case 4: // 中间
                return x >= left && x <= right && y >= top && y <= bottom;
            case 5: // 右边
                return x > right && y >= top && y <= bottom;
            case 6: // 左下角
                return x < left && y > bottom;
            case 7: // 下边
                return x >= left && x <= right && y > bottom;
            case 8: // 右下角
                return x > right && y > bottom;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return "UINinePatch{" +
                "width=" + width +
                ", height=" + height +
                ", patches=[" + patches[0] + ", " + patches[1] + ", " + patches[2] + ", " + patches[3] + "]" +
                '}';
    }
}
