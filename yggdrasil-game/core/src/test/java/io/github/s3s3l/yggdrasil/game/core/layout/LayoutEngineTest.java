package io.github.s3s3l.yggdrasil.game.core.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class LayoutEngineTest {

    @Test
    void excludesDisplayNoneChildrenFromLayoutAndProducesAnEmptyResult() {
        LayoutNode root = node(100, 100);
        LayoutNode hidden = node(50, 50);
        hidden.getStyle().setDisplay(DisplayType.NONE);
        root.addChild(hidden);

        LayoutEngine.layout(root, 100, 100);

        assertEquals(0, hidden.getLayoutResult().getWidth());
        assertEquals(0, hidden.getLayoutResult().getHeight());
    }

    @Test
    void handlesAnEmptyFlexContainerAtZeroViewportWithoutNegativeDimensions() {
        LayoutNode root = node(0, 0);
        root.getStyle().setDisplay(DisplayType.FLEX);
        root.getStyle().getFlexContainer().setJustifyContent(JustifyContent.SPACE_EVENLY);

        LayoutResult result = LayoutEngine.layout(root, 0, 0);

        assertEquals(0, result.getWidth());
        assertEquals(0, result.getHeight());
        assertFalse(result.getContentWidth() < 0);
        assertFalse(result.getContentHeight() < 0);
    }

    @Test
    void preservesChildInsertionOrderForLayoutNodes() {
        LayoutNode root = node(100, 100);
        LayoutNode first = node(10, 10);
        LayoutNode second = node(10, 10);
        root.addChild(first);
        root.addChild(second);

        assertEquals(first, root.childNodes().iterator().next());
    }

    private static LayoutNode node(float width, float height) {
        LayoutStyle style = new LayoutStyle();
        style.getBoxModel().getSize().setWidth(LengthUnit.px(width));
        style.getBoxModel().getSize().setHeight(LengthUnit.px(height));
        return new LayoutNode(style);
    }
}
