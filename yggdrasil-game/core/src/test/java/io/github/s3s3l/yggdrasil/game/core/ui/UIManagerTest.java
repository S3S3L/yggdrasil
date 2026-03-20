package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 管理器单元测试
 */
class UIManagerTest {

    private UIManager uiManager;

    @BeforeEach
    void setUp() {
        uiManager = UIManager.getInstance();
        uiManager.getCanvases().clear();
        uiManager.setEnabled(true);
    }

    @Test
    void testSingleton() {
        UIManager instance1 = UIManager.getInstance();
        UIManager instance2 = UIManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testAddCanvas() {
        UICanvas canvas = new UICanvas("TestCanvas");
        uiManager.addCanvas(canvas);

        assertTrue(uiManager.getCanvases().contains(canvas));
        assertEquals(canvas, uiManager.getActiveCanvas());
    }

    @Test
    void testRemoveCanvas() {
        UICanvas canvas1 = new UICanvas("Canvas1");
        UICanvas canvas2 = new UICanvas("Canvas2");
        uiManager.addCanvas(canvas1);
        uiManager.addCanvas(canvas2);

        uiManager.removeCanvas(canvas1);

        assertFalse(uiManager.getCanvases().contains(canvas1));
        assertEquals(canvas2, uiManager.getActiveCanvas());
    }

    @Test
    void testRemoveActiveCanvas() {
        UICanvas canvas1 = new UICanvas("Canvas1");
        UICanvas canvas2 = new UICanvas("Canvas2");
        uiManager.addCanvas(canvas1);
        uiManager.addCanvas(canvas2);

        uiManager.removeCanvas(canvas2);

        assertEquals(canvas1, uiManager.getActiveCanvas());
    }

    @Test
    void testGetCanvas() {
        UICanvas canvas = new UICanvas("TestCanvas");
        uiManager.addCanvas(canvas);

        UICanvas found = uiManager.getCanvas("TestCanvas");
        assertSame(canvas, found);
    }

    @Test
    void testGetCanvasNotFound() {
        UICanvas found = uiManager.getCanvas("NonExistent");
        assertNull(found);
    }

    @Test
    void testSetEnabled() {
        uiManager.setEnabled(false);
        assertFalse(uiManager.isEnabled());

        uiManager.setEnabled(true);
        assertTrue(uiManager.isEnabled());
    }

    @Test
    void testShowUI() {
        UICanvas canvas = new UICanvas("TestCanvas");
        canvas.setEnabled(false);
        canvas.setVisibility(UIVisibility.HIDDEN);
        uiManager.addCanvas(canvas);

        uiManager.showUI("TestCanvas");

        assertTrue(canvas.isEnabled());
        assertEquals(UIVisibility.VISIBLE, canvas.getVisibility());
    }

    @Test
    void testHideUI() {
        UICanvas canvas = new UICanvas("TestCanvas");
        uiManager.addCanvas(canvas);

        uiManager.hideUI("TestCanvas");

        assertFalse(canvas.isEnabled());
        assertEquals(UIVisibility.HIDDEN, canvas.getVisibility());
    }

    @Test
    void testToggleUI() {
        UICanvas canvas = new UICanvas("TestCanvas");
        uiManager.addCanvas(canvas);

        uiManager.toggleUI("TestCanvas");
        assertFalse(canvas.isEnabled());

        uiManager.toggleUI("TestCanvas");
        assertTrue(canvas.isEnabled());
    }

    @Test
    void testHandleClick() {
        uiManager.setEnabled(false);

        // 禁用时不应处理点击
        assertFalse(uiManager.handleClick(0, 0));
    }

    @Test
    void testHandleHover() {
        uiManager.setEnabled(false);

        // 禁用时不应处理悬停（不抛出异常）
        assertDoesNotThrow(() -> uiManager.handleHover(0, 0));
    }

    @Test
    void testUpdate() {
        uiManager.setEnabled(false);

        // 禁用时不应更新（不抛出异常）
        assertDoesNotThrow(() -> uiManager.update(0.016f));
    }

    @Test
    void testRender() {
        uiManager.setEnabled(false);

        // 禁用时不应渲染（不抛出异常）
        assertDoesNotThrow(() -> uiManager.render());
    }
}
