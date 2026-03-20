package io.github.s3s3l.yggdrasil.game.core.config;

/**
 * 游戏配置示例类
 * 展示如何定义游戏配置
 */
public class GameSettings {

    /**
     * 游戏标题
     */
    private String title = "Yggdrasil";

    /**
     * 屏幕宽度
     */
    private int screenWidth = 1920;

    /**
     * 屏幕高度
     */
    private int screenHeight = 1080;

    /**
     * 是否全屏
     */
    private boolean fullscreen = false;

    /**
     * 音量 (0-100)
     */
    private int volume = 80;

    /**
     * 音乐音量 (0-100)
     */
    private int musicVolume = 70;

    /**
     * 音效音量 (0-100)
     */
    private int sfxVolume = 90;

    /**
     * 图形质量 (low/medium/high/ultra)
     */
    private String graphicsQuality = "high";

    /**
     * 是否显示 FPS
     */
    private boolean showFps = false;

    /**
     * 是否启用垂直同步
     */
    private boolean vsync = true;

    /**
     * 目标帧率 (0 表示无限制)
     */
    private int targetFps = 60;

    /**
     * 语言
     */
    private String language = "zh-CN";

    public GameSettings() {
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(int sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public String getGraphicsQuality() {
        return graphicsQuality;
    }

    public void setGraphicsQuality(String graphicsQuality) {
        this.graphicsQuality = graphicsQuality;
    }

    public boolean isShowFps() {
        return showFps;
    }

    public void setShowFps(boolean showFps) {
        this.showFps = showFps;
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }

    public int getTargetFps() {
        return targetFps;
    }

    public void setTargetFps(int targetFps) {
        this.targetFps = targetFps;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "GameSettings{" +
                "title='" + title + '\'' +
                ", screenWidth=" + screenWidth +
                ", screenHeight=" + screenHeight +
                ", fullscreen=" + fullscreen +
                ", volume=" + volume +
                ", musicVolume=" + musicVolume +
                ", sfxVolume=" + sfxVolume +
                ", graphicsQuality='" + graphicsQuality + '\'' +
                ", showFps=" + showFps +
                ", vsync=" + vsync +
                ", targetFps=" + targetFps +
                ", language='" + language + '\'' +
                '}';
    }
}
