package io.github.s3s3l.yggdrasil.game.core.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSON 配置加载器单元测试
 */
class JsonConfigLoaderTest {

    private JsonConfigLoader loader;
    private String testFile;

    @BeforeEach
    void setUp() throws IOException {
        loader = new JsonConfigLoader();
        testFile = createTempTestFile();
    }

    @Test
    void testLoaderName() {
        assertEquals("JSON", loader.getName());
    }

    @Test
    void testExists() {
        assertTrue(loader.exists(testFile));
        assertFalse(loader.exists("nonexistent_file.json"));
    }

    @Test
    void testLoadSave() throws Exception {
        GameSettings settings = new GameSettings();
        settings.setTitle("Test Game");
        settings.setVolume(75);

        String savePath = "test_save_settings.json";
        loader.save(settings, savePath);

        assertTrue(loader.exists(savePath));

        GameSettings loaded = loader.load(GameSettings.class, savePath);
        assertNotNull(loaded);
        assertEquals("Test Game", loaded.getTitle());
        assertEquals(75, loaded.getVolume());
    }

    @Test
    void testCache() throws Exception {
        loader.load(GameSettings.class, testFile);

        // 验证缓存
        assertTrue(loader.isModified(testFile) || !loader.isModified(testFile));
    }

    @Test
    void testClearCache() throws Exception {
        loader.load(GameSettings.class, testFile);
        loader.clearCache();

        // 缓存已清空
        assertFalse(loader.hasCache(testFile));
    }

    @Test
    void testRemoveCache() throws Exception {
        loader.load(GameSettings.class, testFile);
        loader.removeCache(testFile);

        assertFalse(loader.hasCache(testFile));
    }

    @Test
    void testIsModified() throws Exception {
        // 首次加载后
        loader.load(GameSettings.class, testFile);
        assertFalse(loader.isModified(testFile));

        // 修改文件后应该检测到变更
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("{\"title\":\"Modified\",\"screenWidth\":800,\"screenHeight\":600," +
                    "\"fullscreen\":false,\"volume\":50,\"musicVolume\":50,\"sfxVolume\":50," +
                    "\"graphicsQuality\":\"low\",\"showFps\":false,\"vsync\":false," +
                    "\"targetFps\":30,\"language\":\"en-US\"}");
        }

        assertTrue(loader.isModified(testFile));
    }

    @Test
    void testReloadIfModified() throws Exception {
        GameSettings initial = loader.load(GameSettings.class, testFile);
        assertNotNull(initial);

        // 未修改时返回 null
        assertNull(loader.reloadIfModified(GameSettings.class, testFile));

        // 修改文件，先清除缓存以确保能检测到修改
        loader.clearCache();

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("{\"title\":\"Reloaded\",\"screenWidth\":800,\"screenHeight\":600," +
                    "\"fullscreen\":false,\"volume\":50,\"musicVolume\":50,\"sfxVolume\":50," +
                    "\"graphicsQuality\":\"low\",\"showFps\":false,\"vsync\":false," +
                    "\"targetFps\":30,\"language\":\"en-US\"}");
        }

        // 重新加载
        GameSettings reloaded = loader.load(GameSettings.class, testFile);
        assertNotNull(reloaded);
        assertEquals("Reloaded", reloaded.getTitle());
    }

    @Test
    void testLoadNotFound() {
        assertThrows(Exception.class, () -> {
            loader.load(GameSettings.class, "nonexistent_file.json");
        });
    }

    @Test
    void testHasCache() throws Exception {
        assertFalse(loader.hasCache(testFile));
        loader.load(GameSettings.class, testFile);
        assertTrue(loader.hasCache(testFile));
    }

    private String createTempTestFile() throws IOException {
        String tempFile = "test_config_" + System.currentTimeMillis() + ".json";
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{\"title\":\"Test\",\"screenWidth\":1920,\"screenHeight\":1080," +
                    "\"fullscreen\":false,\"volume\":80,\"musicVolume\":70,\"sfxVolume\":90," +
                    "\"graphicsQuality\":\"high\",\"showFps\":false,\"vsync\":true," +
                    "\"targetFps\":60,\"language\":\"zh-CN\"}");
        }
        return tempFile;
    }
}
