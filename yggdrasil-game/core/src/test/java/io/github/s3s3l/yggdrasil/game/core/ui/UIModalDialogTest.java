package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 模态对话框组件单元测试
 */
class UIModalDialogTest {

    private UIModalDialog dialog;

    @BeforeEach
    void setUp() {
        dialog = new UIModalDialog("Test Dialog");
        dialog.setId(1L);
    }

    @Test
    void testDialogCreation() {
        assertNotNull(dialog);
        assertEquals("Test Dialog", dialog.getTitle());
        assertEquals(UIModalDialog.DialogResult.NONE, dialog.getResult());
        assertTrue(dialog.isDraggable());
        assertTrue(dialog.isResizable());
        assertTrue(dialog.isShowCloseButton());
    }

    @Test
    void testDefaultConstructor() {
        UIModalDialog d = new UIModalDialog();
        assertNotNull(d);
        assertEquals("", d.getTitle());
    }

    @Test
    void testSetTitle() {
        dialog.setTitle("New Title");
        assertEquals("New Title", dialog.getTitle());
    }

    @Test
    void testSetContent() {
        UIComponent content = new UIPanel("Content");
        dialog.setContent(content);

        assertSame(content, dialog.getContent());
        assertSame(dialog, content.getParent());
    }

    @Test
    void testSetMessage() {
        dialog.setMessage("Test Message");
        assertEquals("Test Message", dialog.getMessage());
    }

    @Test
    void testAddButton() {
        dialog.addButton("OK", UIModalDialog.DialogResult.OK);
        dialog.addButton("Cancel", UIModalDialog.DialogResult.CANCEL);

        assertEquals(2, dialog.getButtons().size());
        assertEquals("OK", dialog.getButtons().get(0).text);
    }

    @Test
    void testAddButtonWithFlags() {
        dialog.addButton("OK", UIModalDialog.DialogResult.OK, true, false);
        dialog.addButton("Cancel", UIModalDialog.DialogResult.CANCEL, false, true);

        assertTrue(dialog.getButtons().get(0).isDefault);
        assertTrue(dialog.getButtons().get(1).isCancel);
    }

    @Test
    void testClearButtons() {
        dialog.addButton("OK", UIModalDialog.DialogResult.OK);
        dialog.clearButtons();

        assertEquals(0, dialog.getButtons().size());
    }

    @Test
    void testSetResult() {
        dialog.setResult(UIModalDialog.DialogResult.OK);
        assertEquals(UIModalDialog.DialogResult.OK, dialog.getResult());
    }

    @Test
    void testSetResizable() {
        dialog.setResizable(false);
        assertFalse(dialog.isResizable());
    }

    @Test
    void testSetDraggable() {
        dialog.setDraggable(false);
        assertFalse(dialog.isDraggable());
    }

    @Test
    void testSetTitleHeight() {
        dialog.setTitleHeight(50);
        assertEquals(50, dialog.getTitleHeight(), 0.01f);
    }

    @Test
    void testSetTitleColor() {
        UIColor color = UIColor.BLUE;
        dialog.setTitleColor(color);
        assertEquals(color, dialog.getTitleColor());
    }

    @Test
    void testSetTitleTextColor() {
        UIColor color = UIColor.YELLOW;
        dialog.setTitleTextColor(color);
        assertEquals(color, dialog.getTitleTextColor());
    }

    @Test
    void testSetBackgroundColor() {
        UIColor color = UIColor.GRAY;
        dialog.setBackgroundColor(color);
        assertEquals(color, dialog.getBackgroundColor());
    }

    @Test
    void testSetOverlayColor() {
        UIColor color = new UIColor(0, 0, 0, 0.7f);
        dialog.setOverlayColor(color);
        assertEquals(color, dialog.getOverlayColor());
    }

    @Test
    void testSetOverlayAlpha() {
        dialog.setOverlayAlpha(0.8f);
        assertEquals(0.8f, dialog.getOverlayAlpha(), 0.01f);
    }

    @Test
    void testSetShowCloseButton() {
        dialog.setShowCloseButton(false);
        assertFalse(dialog.isShowCloseButton());
    }

    @Test
    void testGetContentBounds() {
        float[] bounds = dialog.getContentBounds();
        assertNotNull(bounds);
        assertEquals(4, bounds.length);
    }

    @Test
    void testGetButtonAreaBounds() {
        float[] bounds = dialog.getButtonAreaBounds();
        assertNotNull(bounds);
        assertEquals(4, bounds.length);
    }

    @Test
    void testGetButtonBounds() {
        dialog.addButton("OK", UIModalDialog.DialogResult.OK);
        float[] bounds = dialog.getButtonBounds(0);
        assertNotNull(bounds);
        assertEquals(4, bounds.length);
    }

    @Test
    void testGetButtonAtPosition() {
        dialog.addButton("OK", UIModalDialog.DialogResult.OK);
        dialog.addButton("Cancel", UIModalDialog.DialogResult.CANCEL);

        float[] bounds = dialog.getButtonBounds(0);
        int index = dialog.getButtonAtPosition(bounds[0] + 40, bounds[1] + 20);
        assertEquals(0, index);

        index = dialog.getButtonAtPosition(-100, -100);
        assertEquals(-1, index);
    }

    @Test
    void testIsClickOnCloseButton() {
        dialog.setShowCloseButton(true);
        // 关闭按钮在标题栏右侧
        float closeX = dialog.getX() + dialog.getWidth() - 20;
        float closeY = dialog.getY() + dialog.getHeight() - 20;

        assertTrue(dialog.isClickOnCloseButton(closeX, closeY));
        assertFalse(dialog.isClickOnCloseButton(dialog.getX(), dialog.getY()));
    }

    @Test
    void testIsClickOnCloseButton_Disabled() {
        dialog.setShowCloseButton(false);
        assertFalse(dialog.isClickOnCloseButton(1000, 1000));
    }

    @Test
    void testIsClickOnTitleBar() {
        float titleY = dialog.getY() + dialog.getHeight() - 20;
        assertTrue(dialog.isClickOnTitleBar(dialog.getX() + 100, titleY));
        assertFalse(dialog.isClickOnTitleBar(dialog.getX() + 100, dialog.getY()));
    }

    @Test
    void testClose() {
        final UIModalDialog.DialogResult[] result = {null};
        final boolean[] closed = {false};

        dialog.setListener(new UIModalDialog.DialogListener() {
            @Override
            public void onResult(UIModalDialog dialog, UIModalDialog.DialogResult r) {
                result[0] = r;
            }

            @Override
            public void onClosed(UIModalDialog dialog) {
                closed[0] = true;
            }
        });

        dialog.close(UIModalDialog.DialogResult.OK);

        assertEquals(UIModalDialog.DialogResult.OK, result[0]);
        assertTrue(closed[0]);
        assertEquals(UIVisibility.HIDDEN, dialog.getVisibility());
    }

    @Test
    void testShow() {
        dialog.setVisibility(UIVisibility.HIDDEN);
        dialog.show();

        assertEquals(UIVisibility.VISIBLE, dialog.getVisibility());
        assertEquals(UIModalDialog.DialogResult.NONE, dialog.getResult());
    }

    @Test
    void testShowModal() {
        UIComponent parent = new UIPanel("Parent");
        parent.setBounds(0, 0, 800, 600);

        dialog.showModal(parent);

        // 对话框应该在父组件中心
        assertEquals(UIVisibility.VISIBLE, dialog.getVisibility());
    }

    @Test
    void testPack() {
        UIComponent content = new UIPanel("Content");
        content.setBounds(0, 0, 200, 100);
        dialog.setContent(content);

        dialog.pack();

        assertTrue(dialog.getWidth() >= 200);
        assertTrue(dialog.getHeight() > 100);
    }

    @Test
    void testRender() {
        dialog.addButton("OK", UIModalDialog.DialogResult.OK);
        assertDoesNotThrow(() -> dialog.render());
    }

    @Test
    void testRenderHidden() {
        dialog.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> dialog.render());
    }

    @Test
    void testHandleMouseDown_OnCloseButton() {
        float closeX = dialog.getX() + dialog.getWidth() - 20;
        float closeY = dialog.getY() + dialog.getHeight() - 20;

        dialog.handleMouseDown(closeX, closeY);

        // 点击关闭按钮会关闭对话框
        assertEquals(UIVisibility.HIDDEN, dialog.getVisibility());
    }

    @Test
    void testHandleMouseDown_OnTitleBar() {
        dialog.setDraggable(true);
        float titleY = dialog.getY() + dialog.getHeight() - 20;

        dialog.handleMouseDown(dialog.getX() + 100, titleY);

        assertTrue(dialog.isDragging());
    }

    @Test
    void testHandleMouseUp() {
        dialog.setDragging(true);
        dialog.handleMouseUp(0, 0);

        assertFalse(dialog.isDragging());
    }

    @Test
    void testHandleMouseMove_Dragging() {
        dialog.setDragging(true);
        dialog.setDragOffsetX(10);
        dialog.setDragOffsetY(10);

        dialog.handleMouseMove(200, 200);

        assertEquals(190, dialog.getX(), 0.01f);
        assertEquals(190, dialog.getY(), 0.01f);
    }

    @Test
    void testHandleClick_Outside() {
        dialog.show();
        // 模拟点击对话框外部
        dialog.handleClick(-100, -100);

        assertEquals(UIModalDialog.DialogResult.CANCEL, dialog.getResult());
    }

    @Test
    void testListener() {
        final UIModalDialog.DialogResult[] result = {null};
        final boolean[] closed = {false};

        dialog.setListener(new UIModalDialog.DialogListener() {
            @Override
            public void onResult(UIModalDialog dialog, UIModalDialog.DialogResult r) {
                result[0] = r;
            }

            @Override
            public void onClosed(UIModalDialog dialog) {
                closed[0] = true;
            }
        });

        dialog.close(UIModalDialog.DialogResult.YES);

        assertEquals(UIModalDialog.DialogResult.YES, result[0]);
        assertTrue(closed[0]);
    }

    @Test
    void testCreateConfirmDialog() {
        UIModalDialog confirmDialog = UIModalDialog.createConfirmDialog("Confirm", "Are you sure?");
        assertNotNull(confirmDialog);
        assertEquals("Confirm", confirmDialog.getTitle());
        assertEquals("Are you sure?", confirmDialog.getMessage());
        assertEquals(2, confirmDialog.getButtons().size());
    }

    @Test
    void testCreateInfoDialog() {
        UIModalDialog infoDialog = UIModalDialog.createInfoDialog("Info", "Information message");
        assertNotNull(infoDialog);
        assertEquals(1, infoDialog.getButtons().size());
        assertEquals(UIModalDialog.DialogResult.OK, infoDialog.getButtons().get(0).result);
    }

    @Test
    void testCreateErrorDialog() {
        UIModalDialog errorDialog = UIModalDialog.createErrorDialog("Error", "Error message");
        assertNotNull(errorDialog);
        assertEquals(1, errorDialog.getButtons().size());
    }

    @Test
    void testGetPreferredWidth() {
        float width = dialog.getPreferredWidth();
        assertEquals(400, width, 0.01f);
    }

    @Test
    void testGetPreferredHeight() {
        float height = dialog.getPreferredHeight();
        assertEquals(300, height, 0.01f);
    }

    @Test
    void testSetButtons() {
        java.util.List<UIModalDialog.ButtonInfo> buttons = new java.util.ArrayList<>();
        buttons.add(new UIModalDialog.ButtonInfo("Btn1", UIModalDialog.DialogResult.OK));
        buttons.add(new UIModalDialog.ButtonInfo("Btn2", UIModalDialog.DialogResult.CANCEL));

        dialog.setButtons(buttons);

        assertEquals(2, dialog.getButtons().size());
    }

    @Test
    void testSetButtonsNull() {
        dialog.setButtons(null);
        assertEquals(0, dialog.getButtons().size());
    }

    @Test
    void testIsDragging_Default() {
        assertFalse(dialog.isDragging());
    }
}
