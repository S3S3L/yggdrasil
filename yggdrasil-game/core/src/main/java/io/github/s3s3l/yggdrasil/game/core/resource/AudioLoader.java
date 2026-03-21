package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 音频资源加载器
 * 负责加载和卸载音频资源
 */
public class AudioLoader implements ResourceLoader<AudioResource> {

    /**
     * 音频参数缓存
     */
    private final Map<String, AudioParams> audioParams;

    /**
     * 创建音频加载器
     */
    public AudioLoader() {
        this.audioParams = new HashMap<>();
    }

    @Override
    public AudioResource load(String path, LoadParams params) throws IOException {
        // 读取音频文件
        InputStream input = getClass().getResourceAsStream(path);
        byte[] audioData;

        if (input == null) {
            // 从文件系统加载
            audioData = Files.readAllBytes(Paths.get(path));
        } else {
            // 从类路径加载
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int nRead;
            try {
                while ((nRead = input.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                audioData = buffer.toByteArray();
            } finally {
                input.close();
            }
        }

        if (audioData == null || audioData.length == 0) {
            throw new IOException("Failed to load audio: " + path);
        }

        // 获取音频类型
        AudioResource.AudioType type = getAudioType(path, params);

        // 创建音频资源
        AudioResource resource = new AudioResource(path, type, audioData);

        return resource;
    }

    @Override
    public AudioResource load(String path) throws IOException {
        return load(path, null);
    }

    @Override
    public void unload(AudioResource resource) {
        if (resource != null) {
            resource.dispose();
        }
    }

    @Override
    public Class<AudioResource> getResourceType() {
        return AudioResource.class;
    }

    @Override
    public boolean isLoaded(String path) {
        return ResourceManager.get().isLoaded(path);
    }

    /**
     * 设置音频参数
     *
     * @param path   音频路径
     * @param params 音频参数
     */
    public void setAudioParams(String path, AudioParams params) {
        audioParams.put(path, params);
    }

    /**
     * 获取音频类型
     *
     * @param path   音频路径
     * @param params 加载参数
     * @return 音频类型
     */
    private AudioResource.AudioType getAudioType(String path, LoadParams params) {
        if (params != null && params.has("type")) {
            String typeStr = params.getString("type");
            if ("music".equalsIgnoreCase(typeStr)) {
                return AudioResource.AudioType.MUSIC;
            } else if ("sound".equalsIgnoreCase(typeStr)) {
                return AudioResource.AudioType.SOUND;
            }
        }

        // 根据文件扩展名判断
        if (path.endsWith(".wav") || path.endsWith(".ogg")) {
            // 短音频默认为音效
            if (path.contains("sfx") || path.contains("sound")) {
                return AudioResource.AudioType.SOUND;
            }
            // 长音频默认为音乐
            if (path.contains("music") || path.contains("bgm")) {
                return AudioResource.AudioType.MUSIC;
            }
        }

        // 默认为音效
        return AudioResource.AudioType.SOUND;
    }

    /**
     * 音频参数
     */
    public static class AudioParams {
        /**
         * 音量（0.0 - 1.0）
         */
        public float volume = 1.0f;

        /**
         * 是否循环播放
         */
        public boolean looping = false;

        /**
         * 播放优先级（仅对音效有效）
         */
        public int priority = 5;
    }
}
