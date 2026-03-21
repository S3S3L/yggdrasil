package io.github.s3s3l.yggdrasil.game.core.resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 异步资源加载器测试
 */
@DisplayName("异步资源加载器测试")
class AsyncResourceLoaderTest {

    private ResourceManager resourceManager;
    private TestResourceLoader testLoader;

    @BeforeEach
    void setUp() {
        resourceManager = ResourceManager.get();
        testLoader = new TestResourceLoader();
        resourceManager.registerLoader(TestResource.class, testLoader);
    }

    @AfterEach
    void tearDown() {
        resourceManager.clear();
        resourceManager.unregisterLoader(TestResource.class);
    }

    @Test
    @DisplayName("异步加载资源 - 成功")
    void testLoadAsyncSuccess() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] completed = {false};

        AsyncLoadTask<TestResource> task = resourceManager.loadAsync(
            "test/resource.txt",
            TestResource.class,
            new AsyncResourceLoader.LoadCallback<TestResource>() {
                @Override
                public void onComplete(String path, TestResource resource) {
                    completed[0] = true;
                    latch.countDown();
                }

                @Override
                public void onError(String path, Exception exception) {
                    latch.countDown();
                }
            });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "异步加载超时");
        assertTrue(completed[0], "加载未完成");
        assertEquals(AsyncLoadTask.TaskStatus.COMPLETED, task.getStatus());
        assertEquals(1.0f, task.getProgress(), 0.01f);
        assertNotNull(task.getResult());
    }

    @Test
    @DisplayName("异步加载资源 - 失败")
    void testLoadAsyncFailure() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] errorCalled = {false};

        testLoader.shouldFail = true;

        AsyncLoadTask<TestResource> task = resourceManager.loadAsync(
            "test/invalid.txt",
            TestResource.class,
            new AsyncResourceLoader.LoadCallback<TestResource>() {
                @Override
                public void onComplete(String path, TestResource resource) {
                }

                @Override
                public void onError(String path, Exception exception) {
                    errorCalled[0] = true;
                    latch.countDown();
                }
            });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "异步加载超时");
        assertTrue(errorCalled[0], "错误回调未调用");
        assertEquals(AsyncLoadTask.TaskStatus.FAILED, task.getStatus());
        assertNotNull(task.getError());
    }

    @Test
    @DisplayName("异步加载资源 - 取消")
    void testLoadAsyncCancel() throws InterruptedException {
        testLoader.loadDelay = 200; // 增加加载延迟

        AsyncLoadTask<TestResource> task = resourceManager.loadAsync(
            "test/resource.txt",
            TestResource.class,
            new AsyncResourceLoader.LoadCallback<TestResource>() {
                @Override
                public void onComplete(String path, TestResource resource) {
                }
            });

        // 等待任务开始后取消
        Thread.sleep(50);
        task.cancel();

        // 等待一段时间确认任务被取消
        Thread.sleep(100);

        assertTrue(task.isCancelled(), "任务未标记为已取消");
        assertEquals(AsyncLoadTask.TaskStatus.CANCELLED, task.getStatus());
    }

    @Test
    @DisplayName("异步加载资源 - 进度回调")
    void testLoadAsyncProgress() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final AtomicInteger progressCount = new AtomicInteger(0);

        AsyncLoadTask<TestResource> task = resourceManager.loadAsync(
            "test/resource.txt",
            TestResource.class,
            new AsyncResourceLoader.LoadCallback<TestResource>() {
                @Override
                public void onProgress(String path, float progress, String message) {
                    progressCount.incrementAndGet();
                }

                @Override
                public void onComplete(String path, TestResource resource) {
                    latch.countDown();
                }
            });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "异步加载超时");
        assertTrue(progressCount.get() > 0, "进度回调未触发");
    }

    @Test
    @DisplayName("异步加载资源 - 开始回调")
    void testLoadAsyncStart() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] startCalled = {false};

        AsyncLoadTask<TestResource> task = resourceManager.loadAsync(
            "test/resource.txt",
            TestResource.class,
            new AsyncResourceLoader.LoadCallback<TestResource>() {
                @Override
                public void onStart(String path) {
                    startCalled[0] = true;
                }

                @Override
                public void onComplete(String path, TestResource resource) {
                    latch.countDown();
                }
            });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "异步加载超时");
        assertTrue(startCalled[0], "开始回调未调用");
    }

    @Test
    @DisplayName("预加载资源")
    void testPreloadResources() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final int[] progressUpdates = {0};
        final boolean[] completeCalled = {false};

        String[] paths = {"test/resource1.txt", "test/resource2.txt", "test/resource3.txt"};

        resourceManager.preloadResources(paths, TestResource.class, new ResourceManager.PreloadCallback() {
            @Override
            public void onPreloadProgress(int loaded, int total) {
                progressUpdates[0]++;
            }

            @Override
            public void onPreloadComplete(int successCount, int failCount) {
                completeCalled[0] = true;
                assertEquals(3, successCount, "成功加载数量");
                assertEquals(0, failCount, "失败数量");
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "预加载超时");
        assertTrue(completeCalled[0], "完成回调未调用");
        assertTrue(progressUpdates[0] > 0, "进度回调未触发");
    }

    @Test
    @DisplayName("预加载资源 - 部分失败")
    void testPreloadResourcesPartialFailure() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] completeCalled = {false};

        String[] paths = {"test/resource1.txt", "test/invalid.txt"};
        testLoader.shouldFailOnPath = "test/invalid.txt";

        resourceManager.preloadResources(paths, TestResource.class, new ResourceManager.PreloadCallback() {
            @Override
            public void onPreloadProgress(int loaded, int total) {
            }

            @Override
            public void onPreloadComplete(int successCount, int failCount) {
                completeCalled[0] = true;
                assertEquals(1, successCount, "成功加载数量");
                assertEquals(1, failCount, "失败数量");
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "预加载超时");
        assertTrue(completeCalled[0], "完成回调未调用");
    }

    @Test
    @DisplayName("批量加载资源")
    void testBulkLoad() throws InterruptedException {
        String[] paths = {"test/bulk1.txt", "test/bulk2.txt", "test/bulk3.txt"};

        resourceManager.bulkLoad(paths, TestResource.class, ResourceManager.LoadPriority.NORMAL);

        // 等待加载完成
        Thread.sleep(1000);

        // 检查资源是否已加载
        assertTrue(resourceManager.isLoaded("test/bulk1.txt"), "资源 1 应已加载");
        assertTrue(resourceManager.isLoaded("test/bulk2.txt"), "资源 2 应已加载");
        assertTrue(resourceManager.isLoaded("test/bulk3.txt"), "资源 3 应已加载");
    }

    @Test
    @DisplayName("设置线程池大小")
    void testSetThreadPoolSize() {
        assertDoesNotThrow(() -> {
            resourceManager.setThreadPoolSize(8);
        }, "设置线程池大小不应抛出异常");

        assertThrows(IllegalArgumentException.class, () -> {
            resourceManager.setThreadPoolSize(0);
        }, "线程数为 0 应抛出异常");

        assertThrows(IllegalArgumentException.class, () -> {
            resourceManager.setThreadPoolSize(-1);
        }, "负数线程数应抛出异常");
    }

    @Test
    @DisplayName("异步加载已缓存的资源")
    void testLoadAsyncCached() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] completed = {false};

        // 先同步加载缓存资源
        TestResource cached;
        try {
            cached = resourceManager.load("test/cached.txt", TestResource.class);
        } catch (IOException e) {
            fail("加载失败：" + e.getMessage());
            return;
        }
        assertNotNull(cached);

        // 异步加载同一资源
        AsyncLoadTask<TestResource> task = resourceManager.loadAsync(
            "test/cached.txt",
            TestResource.class,
            new AsyncResourceLoader.LoadCallback<TestResource>() {
                @Override
                public void onComplete(String path, TestResource resource) {
                    completed[0] = true;
                    latch.countDown();
                }
            });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "异步加载超时");
        assertTrue(completed[0], "加载未完成");
        assertEquals(AsyncLoadTask.TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    @DisplayName("关闭资源管理器")
    void testShutdown() throws InterruptedException {
        resourceManager.loadAsync("test/resource.txt", TestResource.class, new AsyncResourceLoader.LoadCallback<TestResource>() {
            @Override
            public void onComplete(String path, TestResource resource) {
            }
        });

        // 等待加载开始
        Thread.sleep(50);

        // 关闭资源管理器
        resourceManager.shutdown();

        // 等待一段时间确认资源已释放
        Thread.sleep(100);

        assertEquals(0, resourceManager.getCacheSize(), "缓存应已清空");
    }

    @Test
    @DisplayName("带参数异步加载资源")
    void testLoadAsyncWithParams() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] completed = {false};

        LoadParams params = new LoadParams();

        AsyncLoadTask<TestResource> task = resourceManager.loadAsync(
            "test/resource.txt",
            TestResource.class,
            params,
            new AsyncResourceLoader.LoadCallback<TestResource>() {
                @Override
                public void onComplete(String path, TestResource resource) {
                    completed[0] = true;
                    latch.countDown();
                }
            });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "异步加载超时");
        assertTrue(completed[0], "加载未完成");
        assertEquals(AsyncLoadTask.TaskStatus.COMPLETED, task.getStatus());
    }

    /**
     * 测试资源类
     */
    static class TestResource {
        String content;

        TestResource(String content) {
            this.content = content;
        }
    }

    /**
     * 测试资源加载器
     */
    static class TestResourceLoader implements ResourceLoader<TestResource> {
        boolean shouldFail = false;
        String shouldFailOnPath = null;
        int loadDelay = 0;
        int loadCount = 0;

        @Override
        public TestResource load(String path) throws IOException {
            return load(path, null);
        }

        @Override
        public TestResource load(String path, LoadParams params) throws IOException {
            loadCount++;

            if (loadDelay > 0) {
                try {
                    Thread.sleep(loadDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (shouldFail || (shouldFailOnPath != null && shouldFailOnPath.equals(path))) {
                throw new IOException("加载失败：" + path);
            }

            return new TestResource("content of " + path);
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
