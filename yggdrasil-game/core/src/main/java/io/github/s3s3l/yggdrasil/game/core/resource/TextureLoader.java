package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * 纹理资源加载器
 * 负责加载和卸载纹理资源
 */
public class TextureLoader implements ResourceLoader<TextureResource> {

    /**
     * 纹理参数缓存
     */
    private final Map<String, TextureParams> textureParams;

    /**
     * 创建纹理加载器
     */
    public TextureLoader() {
        this.textureParams = new HashMap<>();
    }

    @Override
    public TextureResource load(String path, LoadParams params) throws IOException {
        // 读取图片文件
        BufferedImage image;

        // 支持从类路径或文件系统加载
        InputStream input = getClass().getResourceAsStream(path);
        if (input == null) {
            input = new ByteArrayInputStream(Files.readAllBytes(Paths.get(path)));
        }

        if (input == null) {
            throw new IOException("Cannot find texture: " + path);
        }

        try {
            image = ImageIO.read(input);
        } finally {
            input.close();
        }

        if (image == null) {
            throw new IOException("Failed to decode image: " + path);
        }

        // 获取纹理参数
        TextureParams texParams = getTextureParams(params);

        // 应用纹理参数
        BufferedImage processed = applyTextureParams(image, texParams);

        // 创建纹理资源（这里使用 BufferedImage 作为底层纹理）
        TextureResource resource = new TextureResource(
                path,
                processed.getWidth(),
                processed.getHeight(),
                processed
        );

        return resource;
    }

    @Override
    public TextureResource load(String path) throws IOException {
        return load(path, null);
    }

    @Override
    public void unload(TextureResource resource) {
        if (resource != null) {
            resource.dispose();
        }
    }

    @Override
    public Class<TextureResource> getResourceType() {
        return TextureResource.class;
    }

    @Override
    public boolean isLoaded(String path) {
        return ResourceManager.get().isLoaded(path);
    }

    /**
     * 设置纹理参数
     *
     * @param path   纹理路径
     * @param params 纹理参数
     */
    public void setTextureParams(String path, TextureParams params) {
        textureParams.put(path, params);
    }

    /**
     * 获取纹理参数
     *
     * @param params 加载参数
     * @return 纹理参数
     */
    private TextureParams getTextureParams(LoadParams params) {
        TextureParams texParams = new TextureParams();

        if (params != null) {
            if (params.has("filter")) {
                texParams.filter = params.getString("filter");
            }
            if (params.has("wrapS")) {
                texParams.wrapS = params.getString("wrapS");
            }
            if (params.has("wrapT")) {
                texParams.wrapT = params.getString("wrapT");
            }
            if (params.has("premultiplyAlpha")) {
                texParams.premultiplyAlpha = params.getBoolean("premultiplyAlpha");
            }
        }

        return texParams;
    }

    /**
     * 应用纹理参数
     *
     * @param image  原始图片
     * @param params 纹理参数
     * @return 处理后的图片
     */
    private BufferedImage applyTextureParams(BufferedImage image, TextureParams params) {
        // 基础实现：返回原图
        // 实际 LibGDX 实现中会应用过滤、环绕等参数
        return image;
    }

    /**
     * 纹理参数
     */
    public static class TextureParams {
        /**
         * 过滤方式：nearest, linear, mipmap
         */
        public String filter = "linear";

        /**
         * S 轴环绕方式：clamp, repeat, mirroredRepeat
         */
        public String wrapS = "clamp";

        /**
         * T 轴环绕方式：clamp, repeat, mirroredRepeat
         */
        public String wrapT = "clamp";

        /**
         * 是否预乘 alpha
         */
        public boolean premultiplyAlpha = false;
    }
}
