package io.github.s3s3l.yggdrasil.game.core.resource;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * ResourceManager 单元测试
 */
public class ResourceManagerTest {

    private ResourceManager resourceManager;
    private TestLoader testLoader;

    @BeforeEach
    void setUp() {
        resourceManager = ResourceManager.get();
        resourceManager.clear();
        testLoader = new TestLoader();
        resourceManager.registerLoader(TestResource.class, testLoader);
    }

    @Test
    void testRegisterLoader() {
        assertNotNull(resourceManager.getLoader(TestResource.class));
    }

    @Test
    void testUnregisterLoader() {
        resourceManager.unregisterLoader(TestResource.class);
        assertNull(resourceManager.getLoader(TestResource.class));
    }

    @Test
    void testLoad() throws IOException {
        TestResource resource = resourceManager.load("test/path", TestResource.class);
        assertNotNull(resource);
        assertEquals("test/path", resource.getPath());
    }

    @Test
    void testLoadWithParams() throws IOException {
        LoadParams params = new LoadParams();
        params.set("quality", "high");

        TestResource resource = resourceManager.load("test/path", TestResource.class, params);
        assertNotNull(resource);
    }

    @Test
    void testLoadNotFound() {
        assertThrows(IllegalStateException.class, () -> {
            resourceManager.load("test/path", String.class);
        });
    }

    @Test
    void testUnload() throws IOException {
        TestResource resource = resourceManager.load("test/path", TestResource.class);
        assertNotNull(resource);

        resourceManager.unload("test/path");
        assertFalse(resourceManager.isLoaded("test/path"));
    }

    @Test
    void testUnloadWithReference() throws IOException {
        TestResource resource1 = resourceManager.load("test/path", TestResource.class);
        TestResource resource2 = resourceManager.load("test/path", TestResource.class);

        // 两次加载应该返回同一个缓存对象
        assertSame(resource1, resource2);

        // 第一次卸载不应该移除资源
        resourceManager.unload("test/path");
        assertTrue(resourceManager.isLoaded("test/path"));

        // 第二次卸载才会移除
        resourceManager.unload("test/path");
        assertFalse(resourceManager.isLoaded("test/path"));
    }

    @Test
    void testUnloadForce() throws IOException {
        TestResource resource = resourceManager.load("test/path", TestResource.class);

        resourceManager.unloadForce("test/path");
        assertFalse(resourceManager.isLoaded("test/path"));
    }

    @Test
    void testClear() throws IOException {
        resourceManager.load("path1", TestResource.class);
        resourceManager.load("path2", TestResource.class);
        resourceManager.load("path3", TestResource.class);

        resourceManager.clear();

        assertEquals(0, resourceManager.getCacheSize());
    }

    @Test
    void testIsLoaded() throws IOException {
        assertFalse(resourceManager.isLoaded("test/path"));

        resourceManager.load("test/path", TestResource.class);

        assertTrue(resourceManager.isLoaded("test/path"));
    }

    @Test
    void testGetCacheSize() throws IOException {
        assertEquals(0, resourceManager.getCacheSize());

        resourceManager.load("path1", TestResource.class);
        resourceManager.load("path2", TestResource.class);

        assertEquals(2, resourceManager.getCacheSize());
    }

    @Test
    void testMaxCacheSize() throws IOException {
        resourceManager.clear();
        resourceManager.setMaxCacheSize(3);

        resourceManager.load("path1", TestResource.class);
        resourceManager.load("path2", TestResource.class);
        resourceManager.load("path3", TestResource.class);
        resourceManager.load("path4", TestResource.class);

        // 应该裁剪到最大大小
        assertTrue(resourceManager.getCacheSize() <= 3,
            "Cache size should be <= 3, but was " + resourceManager.getCacheSize());
    }

    @Test
    void testReferenceCount() throws IOException {
        resourceManager.load("test/path", TestResource.class);
        assertEquals(1, resourceManager.getReferenceCount("test/path"));

        resourceManager.load("test/path", TestResource.class);
        assertEquals(2, resourceManager.getReferenceCount("test/path"));

        resourceManager.unload("test/path");
        assertEquals(1, resourceManager.getReferenceCount("test/path"));
    }

    @Test
    void testGetCached() throws IOException {
        TestResource resource = resourceManager.load("test/path", TestResource.class);
        TestResource cached = resourceManager.getCached("test/path", TestResource.class);

        assertSame(resource, cached);
    }

    /**
     * 测试用资源
     */
    private static class TestResource {
        private final String path;

        public TestResource(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    /**
     * 测试用加载器
     */
    private static class TestLoader implements ResourceLoader<TestResource> {
        @Override
        public TestResource load(String path, LoadParams params) {
            return new TestResource(path);
        }

        @Override
        public TestResource load(String path) {
            return new TestResource(path);
        }

        @Override
        public void unload(TestResource resource) {
        }

        @Override
        public boolean isLoaded(String path) {
            return false;
        }

        @Override
        public Class<TestResource> getResourceType() {
            return TestResource.class;
        }
    }
}
