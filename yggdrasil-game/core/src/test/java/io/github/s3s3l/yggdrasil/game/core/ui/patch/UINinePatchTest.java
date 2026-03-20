package io.github.s3s3l.yggdrasil.game.core.ui.patch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NinePatch 单元测试
 */
class UINinePatchTest {

    private UINinePatch ninePatch;

    @BeforeEach
    void setUp() {
        // 创建一个 100x100 的 NinePatch，可拉伸区域为 10-90
        ninePatch = new UINinePatch(100f, 100f, 10f, 10f, 90f, 90f);
    }

    @Test
    void testGetOriginalSize() {
        assertEquals(100f, ninePatch.getOriginalWidth());
        assertEquals(100f, ninePatch.getOriginalHeight());
    }

    @Test
    void testGetPatches() {
        assertEquals(10f, ninePatch.getLeft());
        assertEquals(10f, ninePatch.getTop());
        assertEquals(90f, ninePatch.getRight());
        assertEquals(90f, ninePatch.getBottom());
    }

    @Test
    void testCreateWithSymmetricPadding() {
        UINinePatch patch = new UINinePatch(100f, 100f, 20f);

        assertEquals(20f, patch.getLeft());
        assertEquals(20f, patch.getTop());
        assertEquals(80f, patch.getRight());
        assertEquals(80f, patch.getBottom());
    }

    @Test
    void testGetDrawRegions() {
        float[][] regions = ninePatch.getDrawRegions(200f, 200f, 0f, 0f);

        // 应该有 9 个区域
        assertEquals(9, regions.length);

        // 每个区域应该有 8 个值 [x, y, width, height, srcX, srcY, srcWidth, srcHeight]
        for (float[] region : regions) {
            assertEquals(8, region.length);
        }

        // 区域 0 - 左上角 (固定)
        assertEquals(0f, regions[0][0]); // x
        assertEquals(0f, regions[0][1]); // y
        assertEquals(10f, regions[0][2]); // width
        assertEquals(10f, regions[0][3]); // height

        // 区域 4 - 中间 (可拉伸)
        assertEquals(10f, regions[4][0]); // x
        assertEquals(10f, regions[4][1]); // y
        assertEquals(180f, regions[4][2]); // width (200 - 10 - 10)
        assertEquals(180f, regions[4][3]); // height (200 - 10 - 10)
    }

    @Test
    void testGetDrawRegionsWithOffset() {
        float[][] regions = ninePatch.getDrawRegions(100f, 100f, 50f, 50f);

        // 区域 0 应该从 (50, 50) 开始
        assertEquals(50f, regions[0][0]);
        assertEquals(50f, regions[0][1]);
    }

    @Test
    void testIsPointInRegion() {
        // 区域 0 - 左上角
        assertTrue(ninePatch.isPointInRegion(0, 5f, 5f));
        assertFalse(ninePatch.isPointInRegion(0, 15f, 5f));

        // 区域 4 - 中间
        assertTrue(ninePatch.isPointInRegion(4, 50f, 50f));
        assertFalse(ninePatch.isPointInRegion(4, 5f, 5f));

        // 区域 8 - 右下角
        assertTrue(ninePatch.isPointInRegion(8, 95f, 95f));
        assertFalse(ninePatch.isPointInRegion(8, 50f, 50f));
    }

    @Test
    void testInvalidRegionIndex() {
        assertFalse(ninePatch.isPointInRegion(-1, 50f, 50f));
        assertFalse(ninePatch.isPointInRegion(9, 50f, 50f));
    }

    @Test
    void testToString() {
        String str = ninePatch.toString();
        assertTrue(str.contains("UINinePatch"));
        assertTrue(str.contains("100.0"));
        assertTrue(str.contains("10.0"));
    }

    @Test
    void testEdgeRegions() {
        float[][] regions = ninePatch.getDrawRegions(100f, 100f, 0f, 0f);

        // 区域 1 - 上边
        assertEquals(10f, regions[1][0]); // x
        assertEquals(0f, regions[1][1]); // y
        assertEquals(80f, regions[1][2]); // width (90 - 10)
        assertEquals(10f, regions[1][3]); // height

        // 区域 3 - 左边
        assertEquals(0f, regions[3][0]); // x
        assertEquals(10f, regions[3][1]); // y
        assertEquals(10f, regions[3][2]); // width
        assertEquals(80f, regions[3][3]); // height (90 - 10)
    }

    @Test
    void testCornerRegions() {
        float[][] regions = ninePatch.getDrawRegions(100f, 100f, 0f, 0f);

        // 区域 2 - 右上角
        assertEquals(90f, regions[2][0]); // x
        assertEquals(0f, regions[2][1]); // y
        assertEquals(10f, regions[2][2]); // width (100 - 90)
        assertEquals(10f, regions[2][3]); // height

        // 区域 6 - 左下角
        assertEquals(0f, regions[6][0]); // x
        assertEquals(90f, regions[6][1]); // y
        assertEquals(10f, regions[6][2]); // width
        assertEquals(10f, regions[6][3]); // height (100 - 90)
    }
}
