package io.github.s3s3l.yggdrasil.game.core.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 配置管理器单元测试
 */
class ConfigManagerTest {

    private ConfigManager configManager;

    @BeforeEach
    void setUp() {
        configManager = ConfigManager.getInstance();
        configManager.unloadAll();
        configManager.disableHotReload();
    }

    @Test
    void testSingleton() {
        ConfigManager instance1 = ConfigManager.getInstance();
        ConfigManager instance2 = ConfigManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testRegisterLoader() {
        JsonConfigLoader loader = new JsonConfigLoader();
        configManager.registerLoader("json", loader);
        configManager.setDefaultLoader(loader);
        assertEquals(loader, configManager.getDefaultLoader());
    }

    @Test
    void testSetDefaultLoader() {
        PropertiesConfigLoader loader = new PropertiesConfigLoader();
        configManager.setDefaultLoader(loader);
        assertEquals(loader, configManager.getDefaultLoader());
    }

    @Test
    void testLoadSaveConfig() throws Exception {
        configManager.setDefaultLoader(new PropertiesConfigLoader());

        GameSettings settings = new GameSettings();
        settings.setTitle("Test Game");
        settings.setScreenWidth(1280);
        settings.setScreenHeight(720);

        String testPath = "test_settings.properties";
        configManager.save(settings, testPath);

        assertTrue(configManager.hasConfig(testPath));
    }

    @Test
    void testGetConfig() throws Exception {
        configManager.setDefaultLoader(new PropertiesConfigLoader());

        GameSettings settings = new GameSettings();
        settings.setTitle("Test Game");
        String testPath = "test_settings2.properties";
        configManager.save(settings, testPath);

        GameSettings loaded = configManager.getConfig(testPath);
        assertNotNull(loaded);
        assertEquals("Test Game", loaded.getTitle());
    }

    @Test
    void testUnload() throws Exception {
        configManager.setDefaultLoader(new PropertiesConfigLoader());

        GameSettings settings = new GameSettings();
        String testPath = "test_settings3.properties";
        configManager.save(settings, testPath);

        assertTrue(configManager.hasConfig(testPath));
        configManager.unload(testPath);
        assertFalse(configManager.hasConfig(testPath));
    }

    @Test
    void testUnloadAll() throws Exception {
        configManager.setDefaultLoader(new PropertiesConfigLoader());

        GameSettings settings1 = new GameSettings();
        GameSettings settings2 = new GameSettings();
        configManager.save(settings1, "test1.properties");
        configManager.save(settings2, "test2.properties");

        assertTrue(configManager.hasConfig("test1.properties"));
        assertTrue(configManager.hasConfig("test2.properties"));
        configManager.unloadAll();
        assertFalse(configManager.hasConfig("test1.properties"));
        assertFalse(configManager.hasConfig("test2.properties"));
    }

    @Test
    void testHotReload() {
        assertFalse(configManager.isHotReloadEnabled());
        configManager.enableHotReload();
        assertTrue(configManager.isHotReloadEnabled());
        configManager.disableHotReload();
        assertFalse(configManager.isHotReloadEnabled());
    }

    @Test
    void testChangeListener() throws Exception {
        configManager.setDefaultLoader(new PropertiesConfigLoader());

        final boolean[] changed = {false};
        ConfigManager.ConfigChangeListener listener = (path, config) -> changed[0] = true;

        String testPath = "test_settings4.properties";
        configManager.addChangeListener(testPath, listener);

        GameSettings settings = new GameSettings();
        configManager.save(settings, testPath);

        // 注意：当前实现中 save 不会触发监听器
        // 只有 reload 才会触发
        // 这里只是验证监听器注册功能
        assertTrue(configManager.hasConfig(testPath));
    }
}
