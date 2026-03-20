package io.github.s3s3l.yggdrasil.game.core.ui.patch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NinePatch 加载器单元测试
 */
class NinePatchLoaderTest {

    private NinePatchLoader loader;

    @BeforeEach
    void setUp() {
        loader = new NinePatchLoader();
    }

    @Test
    void testLoad() {
        UINinePatch patch = loader.load("test", 100f, 100f, 10f, 10f, 90f, 90f);

        assertNotNull(patch);
        assertEquals(100f, patch.getOriginalWidth());
        assertEquals(10f, patch.getLeft());
    }

    @Test
    void testLoadFromConfig() throws Exception {
        String config = "10,20,80,90";
        InputStream stream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));

        UINinePatch patch = loader.loadFromConfig("test", 100f, 100f, stream);

        assertNotNull(patch);
        assertEquals(10f, patch.getLeft());
        assertEquals(20f, patch.getTop());
        assertEquals(80f, patch.getRight());
        assertEquals(90f, patch.getBottom());
    }

    @Test
    void testLoadFromConfigWithSpaces() throws Exception {
        String config = " 10 , 20 , 80 , 90 ";
        InputStream stream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));

        UINinePatch patch = loader.loadFromConfig("test", 100f, 100f, stream);

        assertEquals(10f, patch.getLeft());
        assertEquals(20f, patch.getTop());
    }

    @Test
    void testLoadFromConfigInvalidFormat() {
        String config = "invalid";
        InputStream stream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));

        assertThrows(Exception.class, () -> {
            loader.loadFromConfig("test", 100f, 100f, stream);
        });
    }

    @Test
    void testGet() {
        loader.load("test", 100f, 100f, 10f, 10f, 90f, 90f);
        UINinePatch patch = loader.get("test");

        assertNotNull(patch);
        assertEquals(100f, patch.getOriginalWidth());
    }

    @Test
    void testGetNotFound() {
        UINinePatch patch = loader.get("notexist");
        assertNull(patch);
    }

    @Test
    void testRemove() {
        loader.load("test", 100f, 100f, 10f, 10f, 90f, 90f);
        loader.remove("test");

        assertNull(loader.get("test"));
    }

    @Test
    void testClearCache() {
        loader.load("test1", 100f, 100f, 10f, 10f, 90f, 90f);
        loader.load("test2", 50f, 50f, 5f, 5f, 45f, 45f);

        loader.clearCache();

        assertNull(loader.get("test1"));
        assertNull(loader.get("test2"));
    }

    @Test
    void testGetCachedNames() {
        loader.load("test1", 100f, 100f, 10f, 10f, 90f, 90f);
        loader.load("test2", 50f, 50f, 5f, 5f, 45f, 45f);

        java.util.Set<String> names = loader.getCachedNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("test1"));
        assertTrue(names.contains("test2"));
    }

    @Test
    void testSetResourceLoader() {
        TestResourceLoader testLoader = new TestResourceLoader();
        loader.setResourceLoader(testLoader);

        // Should throw IOException since resource doesn't exist
        assertThrows(Exception.class, () -> {
            loader.loadFromFile("test", 100f, 100f, "/nonexistent");
        });
    }

    private static class TestResourceLoader implements NinePatchLoader.ResourceLoader {
        @Override
        public InputStream getResourceAsStream(String path) {
            return null;
        }
    }
}
