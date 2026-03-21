package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.IOException;

/**
 * 纹理资源
 * 封装纹理相关的数据和操作
 */
public class TextureResource {

    /**
     * 纹理路径
     */
    private final String path;

    /**
     * 纹理宽度
     */
    private final int width;

    /**
     * 纹理高度
     */
    private final int height;

    /**
     * 纹理数据（实际纹理对象）
     */
    private final Object texture;

    /**
     * 是否已加载
     */
    private boolean loaded;

    /**
     * 创建纹理资源
     *
     * @param path   纹理路径
     * @param width  宽度
     * @param height 高度
     * @param texture 底层纹理对象
     */
    public TextureResource(String path, int width, int height, Object texture) {
        this.path = path;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.loaded = true;
    }

    /**
     * 获取纹理路径
     *
     * @return 路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 获取纹理宽度
     *
     * @return 宽度
     */
    public int getWidth() {
        return width;
    }

    /**
     * 获取纹理高度
     *
     * @return 高度
     */
    public int getHeight() {
        return height;
    }

    /**
     * 获取底层纹理对象
     *
     * @return 纹理对象
     */
    public Object getTexture() {
        return texture;
    }

    /**
     * 检查纹理是否已加载
     *
     * @return true 如果已加载
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * 卸载纹理
     */
    public void dispose() {
        loaded = false;
        // 底层纹理对象的释放由加载器处理
    }

    @Override
    public String toString() {
        return "TextureResource{" +
                "path='" + path + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", loaded=" + loaded +
                '}';
    }
}
