package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 图片组件
 * 用于显示纹理/图片的组件
 */
public class UIImage extends UIComponent {

    /**
     * 图片显示类型
     */
    public enum ImageType {
        /**
         * 拉伸模式 - 图片拉伸以填充整个区域
         */
        STRETCH,
        /**
         * 平铺模式 - 图片重复平铺以填充区域
         */
        TILED,
        /**
         * 九宫格模式 - 使用九宫格拉伸，保持角落不变形
         */
        NINE_PATCH
    }

    private Object textureRegion;                 // 纹理区域（使用 Object 避免依赖 LibGDX）
    private UIColor colorTint;                    // 颜色叠加
    private ImageType imageType;                  // 图片显示类型
    private float scale;                          // 缩放比例（用于平铺模式）
    private boolean preserveAspectRatio;          // 是否保持宽高比
    private ImageTypeListener listener;           // 图片类型变化监听器

    public UIImage() {
        this(null);
    }

    public UIImage(Object textureRegion) {
        this.textureRegion = textureRegion;
        this.colorTint = UIColor.WHITE;
        this.imageType = ImageType.STRETCH;
        this.scale = 1.0f;
        this.preserveAspectRatio = false;
    }

    public UIImage(String texturePath) {
        this((Object) null);
        // 纹理路径存储，实际加载由渲染层实现
    }

    public Object getTextureRegion() {
        return textureRegion;
    }

    public void setTextureRegion(Object textureRegion) {
        this.textureRegion = textureRegion;
    }

    /**
     * 设置纹理
     *
     * @param texture 纹理对象（由渲染层定义）
     */
    public void setTexture(Object texture) {
        this.textureRegion = texture;
    }

    /**
     * 设置纹理
     *
     * @param texturePath 纹理路径
     */
    public void setTexture(String texturePath) {
        // 纹理路径存储，实际加载由渲染层实现
    }

    public UIColor getColorTint() {
        return colorTint;
    }

    public void setColorTint(UIColor colorTint) {
        this.colorTint = colorTint;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
        if (listener != null) {
            listener.onImageTypeChanged(this, imageType);
        }
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isPreserveAspectRatio() {
        return preserveAspectRatio;
    }

    public void setPreserveAspectRatio(boolean preserveAspectRatio) {
        this.preserveAspectRatio = preserveAspectRatio;
    }

    public ImageTypeListener getListener() {
        return listener;
    }

    public void setListener(ImageTypeListener listener) {
        this.listener = listener;
    }

    /**
     * 获取图片宽度（考虑缩放）
     *
     * @return 图片宽度
     */
    public float getImageWidth() {
        // 默认返回组件宽度
        return getWidth();
    }

    /**
     * 获取图片高度（考虑缩放）
     *
     * @return 图片高度
     */
    public float getImageHeight() {
        // 默认返回组件高度
        return getHeight();
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染图片
        renderImage();
    }

    /**
     * 渲染图片
     */
    protected void renderImage() {
        // 实际渲染由游戏引擎实现
        // 根据 imageType 选择不同的渲染方式：
        // - STRETCH: 拉伸图片填充 bounds
        // - TILED: 使用 scale 平铺图片
        // - NINE_PATCH: 九宫格拉伸
    }

    /**
     * 图片类型变化监听器
     */
    public interface ImageTypeListener {
        /**
         * 当图片类型改变时调用
         *
         * @param image 图片组件
         * @param newType 新的图片类型
         */
        void onImageTypeChanged(UIImage image, ImageType newType);
    }
}
