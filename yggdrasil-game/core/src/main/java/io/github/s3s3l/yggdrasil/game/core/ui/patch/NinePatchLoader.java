package io.github.s3s3l.yggdrasil.game.core.ui.patch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * NinePatch 加载器
 * 从文件和配置加载 NinePatch 资源
 */
public class NinePatchLoader {

    private final Map<String, UINinePatch> cache;
    private ResourceLoader resourceLoader;

    /**
     * 创建 NinePatch 加载器
     */
    public NinePatchLoader() {
        this.cache = new HashMap<>();
        this.resourceLoader = new DefaultResourceLoader();
    }

    /**
     * 设置资源加载器
     *
     * @param resourceLoader 资源加载器
     */
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 加载 NinePatch
     *
     * @param name   NinePatch 名称
     * @param width  原始宽度
     * @param height 原始高度
     * @param left   左边可拉伸区域起始位置
     * @param top    顶部可拉伸区域起始位置
     * @param right  右边可拉伸区域结束位置
     * @param bottom 底部可拉伸区域结束位置
     * @return 加载的 NinePatch
     */
    public UINinePatch load(String name, float width, float height, float left, float top, float right, float bottom) {
        UINinePatch ninePatch = new UINinePatch(width, height, left, top, right, bottom);
        cache.put(name, ninePatch);
        return ninePatch;
    }

    /**
     * 从配置加载 NinePatch
     * 配置格式: left,top,right,bottom
     *
     * @param name         NinePatch 名称
     * @param width        原始宽度
     * @param height       原始高度
     * @param configStream 配置流
     * @return 加载的 NinePatch
     * @throws IOException 读取失败
     */
    public UINinePatch loadFromConfig(String name, float width, float height, InputStream configStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(configStream, StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    float left = Float.parseFloat(parts[0].trim());
                    float top = Float.parseFloat(parts[1].trim());
                    float right = Float.parseFloat(parts[2].trim());
                    float bottom = Float.parseFloat(parts[3].trim());
                    return load(name, width, height, left, top, right, bottom);
                }
            }
        }
        throw new IOException("Invalid NinePatch config format");
    }

    /**
     * 从文件加载 NinePatch
     *
     * @param name       NinePatch 名称
     * @param width      原始宽度
     * @param height     原始高度
     * @param configPath 配置文件路径
     * @return 加载的 NinePatch
     * @throws IOException 读取失败
     */
    public UINinePatch loadFromFile(String name, float width, float height, String configPath) throws IOException {
        try (InputStream stream = resourceLoader.getResourceAsStream(configPath)) {
            if (stream != null) {
                return loadFromConfig(name, width, height, stream);
            }
        }
        throw new IOException("Cannot load NinePatch from: " + configPath);
    }

    /**
     * 获取缓存的 NinePatch
     *
     * @param name NinePatch 名称
     * @return NinePatch，如果没有则返回 null
     */
    public UINinePatch get(String name) {
        return cache.get(name);
    }

    /**
     * 移除缓存的 NinePatch
     *
     * @param name NinePatch 名称
     */
    public void remove(String name) {
        cache.remove(name);
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * 获取所有缓存的 NinePatch 名称
     *
     * @return 名称集合
     */
    public java.util.Set<String> getCachedNames() {
        return cache.keySet();
    }

    /**
     * 资源加载器接口
     */
    public interface ResourceLoader {
        /**
         * 获取资源流
         *
         * @param path 资源路径
         * @return 输入流
         */
        InputStream getResourceAsStream(String path);
    }

    /**
     * 默认资源加载器
     */
    public static class DefaultResourceLoader implements ResourceLoader {
        @Override
        public InputStream getResourceAsStream(String path) {
            // 首先尝试类路径
            InputStream stream = NinePatchLoader.class.getResourceAsStream(path);
            if (stream == null) {
                // 尝试使用上下文类加载器
                stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            }
            return stream;
        }
    }
}
