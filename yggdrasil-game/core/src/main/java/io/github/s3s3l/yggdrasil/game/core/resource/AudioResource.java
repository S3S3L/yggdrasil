package io.github.s3s3l.yggdrasil.game.core.resource;

/**
 * 音频资源
 * 封装音频相关的数据和操作
 */
public class AudioResource {

    /**
     * 音频路径
     */
    private final String path;

    /**
     * 音频类型
     */
    private final AudioType type;

    /**
     * 音频数据
     */
    private final byte[] audioData;

    /**
     * 是否已加载
     */
    private boolean loaded;

    /**
     * 音频类型
     */
    public enum AudioType {
        /**
         * 音效（短音频，可重复播放）
         */
        SOUND,
        /**
         * 音乐（长音频，可循环播放）
         */
        MUSIC
    }

    /**
     * 创建音频资源
     *
     * @param path 音频路径
     * @param type 音频类型
     * @param audioData 音频数据
     */
    public AudioResource(String path, AudioType type, byte[] audioData) {
        this.path = path;
        this.type = type;
        this.audioData = audioData;
        this.loaded = true;
    }

    /**
     * 获取音频路径
     *
     * @return 路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 获取音频类型
     *
     * @return 类型
     */
    public AudioType getType() {
        return type;
    }

    /**
     * 获取音频数据
     *
     * @return 音频数据
     */
    public byte[] getAudioData() {
        return audioData;
    }

    /**
     * 检查音频是否已加载
     *
     * @return true 如果已加载
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * 卸载音频
     */
    public void dispose() {
        loaded = false;
    }

    /**
     * 获取音频时长（毫秒）
     * 实际实现需要从音频数据解析
     *
     * @return 时长（毫秒）
     */
    public long getDuration() {
        // 简化实现，实际需要从音频数据解析
        return 0;
    }

    @Override
    public String toString() {
        return "AudioResource{" +
                "path='" + path + '\'' +
                ", type=" + type +
                ", loaded=" + loaded +
                '}';
    }
}
