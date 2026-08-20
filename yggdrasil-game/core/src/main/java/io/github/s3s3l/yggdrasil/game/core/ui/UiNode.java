package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.github.s3s3l.yggdrasil.game.core.layout.DisplayType;
import io.github.s3s3l.yggdrasil.game.core.layout.LayoutNode;
import io.github.s3s3l.yggdrasil.game.core.layout.LayoutResult;
import io.github.s3s3l.yggdrasil.game.core.layout.LayoutStyle;
import io.github.s3s3l.yggdrasil.game.core.layout.OverflowType;
import io.github.s3s3l.yggdrasil.game.core.layout.Visibility;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.DrawCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.PopClipCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.PushClipCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventListener;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventPhase;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventType;

/**
 * A backend-neutral UI tree node. Child insertion order is retained as the
 * tie-breaker for equal z-index values.
 */
public class UiNode extends LayoutNode {

    private final Map<UiEventType, Map<UiEventPhase, List<UiEventListener>>> listeners =
            new EnumMap<>(UiEventType.class);
    private int zIndex;
    private boolean enabled = true;
    private boolean focusable;
    private boolean clipChildren;
    private UiNodeDrawHandler drawHandler;

    public UiNode() {
        super();
    }

    public UiNode(String name, LayoutStyle style) {
        super(name, style);
    }

    public void addChild(UiNode child) {
        super.addChild(Objects.requireNonNull(child, "child"));
    }

    public void removeChild(UiNode child) {
        super.removeChild(Objects.requireNonNull(child, "child"));
    }

    public List<UiNode> uiChildren() {
        List<UiNode> result = new ArrayList<>();
        for (LayoutNode child : childNodes()) {
            if (child instanceof UiNode) {
                result.add((UiNode) child);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public UiNode getUiParent() {
        return parent() instanceof UiNode ? (UiNode) parent() : null;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean isClipChildren() {
        LayoutStyle style = getStyle();
        return clipChildren || style.getOverflow() != OverflowType.VISIBLE
                || style.getOverflowX() != OverflowType.VISIBLE || style.getOverflowY() != OverflowType.VISIBLE;
    }

    public void setClipChildren(boolean clipChildren) {
        this.clipChildren = clipChildren;
    }

    public void setDrawHandler(UiNodeDrawHandler drawHandler) {
        this.drawHandler = drawHandler;
    }

    public UiListenerRegistration addEventListener(UiEventType type, UiEventPhase phase, UiEventListener listener) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(phase, "phase");
        Objects.requireNonNull(listener, "listener");
        List<UiEventListener> registrations = listeners
                .computeIfAbsent(type, ignored -> new EnumMap<>(UiEventPhase.class))
                .computeIfAbsent(phase, ignored -> new ArrayList<>());
        registrations.add(listener);
        return () -> registrations.remove(listener);
    }

    public boolean isVisible() {
        LayoutStyle style = getStyle();
        return style.getDisplay() != DisplayType.NONE && style.getVisibility() == Visibility.VISIBLE;
    }

    public UiRect getBounds() {
        LayoutResult result = getLayoutResult();
        if (result == null) {
            return new UiRect(0, 0, 0, 0);
        }
        return new UiRect(result.getX(), result.getY(), Math.max(0, result.getWidth()), Math.max(0, result.getHeight()));
    }

    public UiRect getContentBounds() {
        LayoutResult result = getLayoutResult();
        if (result == null) {
            return new UiRect(0, 0, 0, 0);
        }
        if (getComputedStyle() == null) {
            return getBounds();
        }
        float x = result.getX() + getComputedStyle().getPaddingLeft();
        float y = result.getY() + getComputedStyle().getPaddingTop();
        return new UiRect(x, y, Math.max(0, result.getContentWidth()), Math.max(0, result.getContentHeight()));
    }

    public UiNode hitTest(float x, float y) {
        return hitTest(x, y, true);
    }

    boolean isDescendantOf(UiNode ancestor) {
        for (UiNode node = this; node != null; node = node.getUiParent()) {
            if (node == ancestor) {
                return true;
            }
        }
        return false;
    }

    List<UiEventListener> listeners(UiEventType type, UiEventPhase phase) {
        Map<UiEventPhase, List<UiEventListener>> byPhase = listeners.get(type);
        if (byPhase == null || byPhase.get(phase) == null) {
            return List.of();
        }
        return List.copyOf(byPhase.get(phase));
    }

    void collectDrawCommands(List<DrawCommand> commands, Set<UiNode> omitted) {
        if (!isVisible() || omitted.contains(this)) {
            return;
        }
        UiDrawContext context = new UiDrawContext(commands);
        if (drawHandler != null) {
            drawHandler.draw(this, context);
        }
        boolean clipped = isClipChildren();
        if (clipped) {
            commands.add(new PushClipCommand(getContentBounds()));
        }
        for (UiNode child : orderedChildren()) {
            child.collectDrawCommands(commands, omitted);
        }
        if (clipped) {
            commands.add(PopClipCommand.INSTANCE);
        }
    }

    List<UiNode> orderedChildren() {
        List<UiNode> children = new ArrayList<>(uiChildren());
        children.sort(Comparator.comparingInt(UiNode::getZIndex));
        return children;
    }

    private UiNode hitTest(float x, float y, boolean requireBounds) {
        if (!enabled || !isVisible()) {
            return null;
        }
        boolean inside = getBounds().contains(x, y);
        if (requireBounds && !inside) {
            return null;
        }
        if (isClipChildren() && !getContentBounds().contains(x, y)) {
            return inside ? this : null;
        }
        List<UiNode> children = orderedChildren();
        for (int index = children.size() - 1; index >= 0; index--) {
            UiNode hit = children.get(index).hitTest(x, y, false);
            if (hit != null) {
                return hit;
            }
        }
        return inside ? this : null;
    }
}
