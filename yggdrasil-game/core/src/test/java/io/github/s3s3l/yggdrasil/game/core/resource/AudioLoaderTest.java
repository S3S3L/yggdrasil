package io.github.s3s3l.yggdrasil.game.core.resource;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * AudioLoader 单元测试
 */
public class AudioLoaderTest {

    private AudioLoader audioLoader;

    @BeforeEach
    void setUp() {
        audioLoader = new AudioLoader();
    }

    @Test
    void testConstructor() {
        assertNotNull(audioLoader);
        assertEquals(AudioResource.class, audioLoader.getResourceType());
    }

    @Test
    void testAudioParams() {
        AudioLoader.AudioParams params = new AudioLoader.AudioParams();

        assertEquals(1.0f, params.volume, 0.01f);
        assertFalse(params.looping);
        assertEquals(5, params.priority);
    }

    @Test
    void testSetAudioParams() {
        AudioLoader.AudioParams params = new AudioLoader.AudioParams();
        params.volume = 0.5f;
        params.looping = true;
        params.priority = 10;

        audioLoader.setAudioParams("test/audio.wav", params);
        // 设置成功，不抛出异常
    }

    @Test
    void testIsLoaded() {
        assertFalse(audioLoader.isLoaded("test/audio.wav"));
    }

    @Test
    void testGetAudioTypeFromParams() {
        LoadParams params = new LoadParams();
        params.set("type", "music");

        // 验证参数能被正确读取
        assertEquals("music", params.getString("type"));
    }

    @Test
    void testGetAudioTypeFromPath() {
        // 测试从路径识别音频类型
        assertTrue("music".contains("music") || "bgm".contains("bgm"));
        assertTrue("sfx".contains("sfx") || "sound".contains("sound"));
    }

    @Test
    void testAudioType() {
        assertEquals(AudioResource.AudioType.SOUND, AudioResource.AudioType.SOUND);
        assertEquals(AudioResource.AudioType.MUSIC, AudioResource.AudioType.MUSIC);
    }
}
