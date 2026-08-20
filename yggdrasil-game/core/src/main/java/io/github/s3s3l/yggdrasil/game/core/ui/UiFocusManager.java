package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.Objects;

/**
 * Stores the single focused node for a UI scene.
 */
public final class UiFocusManager {

    private UiNode focusedNode;

    public UiNode getFocusedNode() {
        return focusedNode;
    }

    public boolean requestFocus(UiNode node) {
        Objects.requireNonNull(node, "node");
        if (!node.isFocusable() || !node.isEnabled() || !node.isVisible()) {
            return false;
        }
        focusedNode = node;
        return true;
    }

    public void clearFocus() {
        focusedNode = null;
    }
}
