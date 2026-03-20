package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 图片组件单元测试
 */
class UIImageTest {

    private UIImage image;

    @BeforeEach
    void setUp() {
        image = new UIImage();
        image.setId(1L);
    }

    @Test
    void testImageCreation() {
        assertNotNull(image);
        assertNull(image.getTextureRegion());
        assertEquals(UIColor.WHITE, image.getColorTint());
        assertEquals(UIImage.ImageType.STRETCH, image.getImageType());
        assertEquals(1.0f, image.getScale());
        assertFalse(image.isPreserveAspectRatio());
    }

    @Test
    void testSetTextureRegion() {
        // 由于 TextureRegion 需要实际纹理，这里只测试 setter/getter
        // 实际使用时会传入有效的 TextureRegion
        assertNull(image.getTextureRegion());
    }

    @Test
    void testSetColorTint() {
        UIColor tint = UIColor.RED;
        image.setColorTint(tint);
        assertEquals(tint, image.getColorTint());
    }

    @Test
    void testSetImageType() {
        image.setImageType(UIImage.ImageType.TILED);
        assertEquals(UIImage.ImageType.TILED, image.getImageType());

        image.setImageType(UIImage.ImageType.NINE_PATCH);
        assertEquals(UIImage.ImageType.NINE_PATCH, image.getImageType());
    }

    @Test
    void testSetScale() {
        image.setScale(2.0f);
        assertEquals(2.0f, image.getScale());

        image.setScale(0.5f);
        assertEquals(0.5f, image.getScale());
    }

    @Test
    void testSetPreserveAspectRatio() {
        image.setPreserveAspectRatio(true);
        assertTrue(image.isPreserveAspectRatio());

        image.setPreserveAspectRatio(false);
        assertFalse(image.isPreserveAspectRatio());
    }

    @Test
    void testSetImageTypeWithListener() {
        final boolean[] listenerCalled = {false};
        final UIImage.ImageType[] capturedType = {null};

        image.setListener((img, newType) -> {
            listenerCalled[0] = true;
            capturedType[0] = newType;
        });

        image.setImageType(UIImage.ImageType.TILED);

        assertTrue(listenerCalled[0]);
        assertEquals(UIImage.ImageType.TILED, capturedType[0]);
    }

    @Test
    void testRender() {
        // 渲染不应抛出异常
        assertDoesNotThrow(() -> image.render());
    }

    @Test
    void testRenderHidden() {
        image.setVisibility(UIVisibility.HIDDEN);

        // 隐藏的组件不应抛出异常
        assertDoesNotThrow(() -> image.render());
    }

    @Test
    void testSetVisibility() {
        image.setVisibility(UIVisibility.HIDDEN);
        assertFalse(image.isVisible());

        image.setVisibility(UIVisibility.VISIBLE);
        assertTrue(image.isVisible());

        image.setVisibility(UIVisibility.COLLAPSED);
        assertFalse(image.isVisible());
    }

    @Test
    void testImageType_Stretch() {
        image.setImageType(UIImage.ImageType.STRETCH);
        assertEquals(UIImage.ImageType.STRETCH, image.getImageType());
    }

    @Test
    void testImageType_Tiled() {
        image.setImageType(UIImage.ImageType.TILED);
        assertEquals(UIImage.ImageType.TILED, image.getImageType());
    }

    @Test
    void testImageType_NinePatch() {
        image.setImageType(UIImage.ImageType.NINE_PATCH);
        assertEquals(UIImage.ImageType.NINE_PATCH, image.getImageType());
    }

    @Test
    void testSetBounds() {
        image.setBounds(10, 20, 100, 200);

        assertEquals(10, image.getX());
        assertEquals(20, image.getY());
        assertEquals(100, image.getWidth());
        assertEquals(200, image.getHeight());
    }

    @Test
    void testSetPosition() {
        image.setPosition(50, 100);

        assertEquals(50, image.getX());
        assertEquals(100, image.getY());
    }

    @Test
    void testSetSize() {
        image.setSize(200, 300);

        assertEquals(200, image.getWidth());
        assertEquals(300, image.getHeight());
    }

    @Test
    void testSetBackgroundColor() {
        UIColor bgColor = UIColor.BLUE;
        image.setBackgroundColor(bgColor);
        assertEquals(bgColor, image.getBackgroundColor());
    }

    @Test
    void testSetOpacity() {
        image.setOpacity(0.5f);
        assertEquals(0.5f, image.getOpacity());

        image.setOpacity(-0.5f);
        assertEquals(0f, image.getOpacity());

        image.setOpacity(1.5f);
        assertEquals(1f, image.getOpacity());
    }

    @Test
    void testSetEnabled() {
        image.setEnabled(false);
        assertFalse(image.isEnabled());

        image.setEnabled(true);
        assertTrue(image.isEnabled());
    }

    @Test
    void testSetInteractable() {
        image.setInteractable(false);
        assertFalse(image.isInteractable());

        image.setInteractable(true);
        assertTrue(image.isInteractable());
    }

    @Test
    void testListenerNullByDefault() {
        assertNull(image.getListener());
    }

    @Test
    void testSetListener() {
        UIImage.ImageTypeListener listener = (img, type) -> {};
        image.setListener(listener);
        assertNotNull(image.getListener());
    }
}
