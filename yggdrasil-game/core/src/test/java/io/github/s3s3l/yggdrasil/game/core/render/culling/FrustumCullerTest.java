package io.github.s3s3l.yggdrasil.game.core.render.culling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 视锥剔除器测试
 */
@DisplayName("视锥剔除器测试")
class FrustumCullerTest {

    private FrustumCuller culler;
    private ViewFrustum frustum;

    @BeforeEach
    void setUp() {
        frustum = new ViewFrustum(0, 0, 100, 100);
        culler = new FrustumCuller(frustum);
    }

    @Test
    @DisplayName("视锥包含点")
    void testFrustumContainsPoint() {
        assertTrue(frustum.contains(0, 0));
        assertTrue(frustum.contains(25, 25));
        assertTrue(frustum.contains(-25, -25));
        assertFalse(frustum.contains(100, 100));
        assertFalse(frustum.contains(-100, -100));
    }

    @Test
    @DisplayName("视锥与矩形相交")
    void testFrustumIntersectsRect() {
        // 完全在视锥内
        assertTrue(frustum.intersects(0, 0, 10, 10));

        // 与视锥相交
        assertTrue(frustum.intersects(40, 40, 20, 20));

        // 完全在视锥外
        assertFalse(frustum.intersects(100, 100, 10, 10));
    }

    @Test
    @DisplayName("剔除对象列表")
    void testCullObjects() {
        List<TestCullable> objects = Arrays.asList(
            new TestCullable(0, 0, 10, 10),    // 可见
            new TestCullable(40, 40, 20, 20),  // 可见
            new TestCullable(100, 100, 10, 10) // 不可见
        );

        List<TestCullable> visible = culler.cull(objects);

        assertEquals(2, visible.size());
    }

    @Test
    @DisplayName("剔除统计信息")
    void testCullStats() {
        List<TestCullable> objects = Arrays.asList(
            new TestCullable(0, 0, 10, 10),
            new TestCullable(100, 100, 10, 10)
        );

        culler.cull(objects);

        CullStats stats = culler.getStats();
        assertEquals(1, stats.getCullCount());
        assertEquals(2, stats.getCheckedObjects());
        assertEquals(1, stats.getCulledObjects());
    }

    @Test
    @DisplayName("禁用剔除")
    void testDisableCulling() {
        culler.setEnabled(false);

        List<TestCullable> objects = Arrays.asList(
            new TestCullable(0, 0, 10, 10),
            new TestCullable(100, 100, 10, 10)
        );

        List<TestCullable> visible = culler.cull(objects);

        // 禁用剔除时，所有对象都可见
        assertEquals(2, visible.size());
    }

    @Test
    @DisplayName("更新视锥")
    void testUpdateFrustum() {
        culler.updateFrustum(50, 50, 200, 200);

        // 原来不可见的物体现在应该可见
        TestCullable farObject = new TestCullable(100, 100, 10, 10);
        assertTrue(culler.isVisible(farObject));
    }

    @Test
    @DisplayName("隐藏对象不可见")
    void testHiddenObjectInvisible() {
        TestCullable hiddenObject = new TestCullable(0, 0, 10, 10);
        hiddenObject.setVisible(false);

        assertFalse(culler.isVisible(hiddenObject));
    }

    /**
     * 测试用可剔除对象
     */
    static class TestCullable implements Cullable {
        private final float centerX;
        private final float centerY;
        private final float width;
        private final float height;
        private boolean visible = true;

        TestCullable(float centerX, float centerY, float width, float height) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.width = width;
            this.height = height;
        }

        @Override
        public float getCenterX() {
            return centerX;
        }

        @Override
        public float getCenterY() {
            return centerY;
        }

        @Override
        public float getBoundingWidth() {
            return width;
        }

        @Override
        public float getBoundingHeight() {
            return height;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }
    }
}
