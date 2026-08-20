package io.github.s3s3l.yggdrasil.game.core.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LayoutNodeTest {

    @Test
    void reparentsChildrenWithoutLeavingThemInTheOldTree() {
        LayoutNode firstParent = new LayoutNode();
        LayoutNode secondParent = new LayoutNode();
        LayoutNode child = new LayoutNode();
        firstParent.addChild(child);

        secondParent.addChild(child);

        assertEquals(0, firstParent.childNodes().size());
        assertEquals(secondParent, child.parent());
    }

    @Test
    void rejectsCycles() {
        LayoutNode root = new LayoutNode();
        LayoutNode child = new LayoutNode();
        root.addChild(child);

        assertThrows(IllegalArgumentException.class, () -> child.addChild(root));
    }
}
