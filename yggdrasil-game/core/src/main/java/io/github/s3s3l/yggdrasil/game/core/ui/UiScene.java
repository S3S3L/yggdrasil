package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.game.core.layout.LayoutEngine;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.DrawCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventType;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiInputDispatcher;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiInputEvent;

/**
 * A pure UI scene that can be driven by {@link io.github.s3s3l.yggdrasil.game.core.runtime.GameRuntime}.
 * Rendering frontends consume its immutable draw-command snapshot after pre-render.
 */
public final class UiScene implements Renderable, UiInputDispatcher {

    private static final AtomicLong IDS = new AtomicLong();

    private long id = IDS.incrementAndGet();
    private final UiNode root;
    private final UiFocusManager focusManager = new UiFocusManager();
    private final UiEventDispatcher eventDispatcher;
    private final List<UiNode> modals = new ArrayList<>();
    private final Map<UiNode, UiNode> modalPreviousFocus = new HashMap<>();
    private final Set<UiNode> sceneAttachedModals = new LinkedHashSet<>();
    private UiViewport viewport;
    private List<DrawCommand> drawCommands = List.of();
    private boolean dirty = true;

    public UiScene(UiNode root, UiViewport viewport) {
        this(root, viewport, ignored -> {
        });
    }

    public UiScene(UiNode root, UiViewport viewport, Consumer<Throwable> eventFailureHandler) {
        this.root = Objects.requireNonNull(root, "root");
        this.viewport = Objects.requireNonNull(viewport, "viewport");
        this.eventDispatcher = new UiEventDispatcher(eventFailureHandler);
    }

    public UiNode getRoot() {
        return root;
    }

    public UiFocusManager focus() {
        return focusManager;
    }

    public UiViewport getViewport() {
        return viewport;
    }

    public void setViewport(UiViewport viewport) {
        this.viewport = Objects.requireNonNull(viewport, "viewport");
        markDirty();
    }

    public List<DrawCommand> getDrawCommands() {
        return drawCommands;
    }

    public UiNode getActiveModal() {
        return modals.isEmpty() ? null : modals.get(modals.size() - 1);
    }

    public void showModal(UiNode modal) {
        Objects.requireNonNull(modal, "modal");
        if (!modal.isDescendantOf(root)) {
            root.addChild(modal);
            sceneAttachedModals.add(modal);
        }
        modals.remove(modal);
        modals.add(modal);
        modalPreviousFocus.put(modal, focusManager.getFocusedNode());
        if (focusManager.getFocusedNode() == null || !focusManager.getFocusedNode().isDescendantOf(modal)) {
            UiNode focusable = firstFocusable(modal);
            if (focusable != null) {
                focusManager.requestFocus(focusable);
            } else {
                focusManager.clearFocus();
            }
        }
        markDirty();
    }

    public boolean dismissModal(UiNode modal) {
        int index = modals.indexOf(modal);
        boolean removed = index >= 0;
        boolean wasActive = index == modals.size() - 1;
        UiNode previousFocus = modalPreviousFocus.remove(modal);
        if (removed) {
            modals.remove(index);
        }
        if (removed && wasActive
                && (focusManager.getFocusedNode() == null || focusManager.getFocusedNode().isDescendantOf(modal))) {
            UiNode activeModal = getActiveModal();
            UiNode focusable = activeModal == null ? previousFocus : firstFocusable(activeModal);
            if (focusable == null) {
                focusManager.clearFocus();
            } else {
                focusManager.requestFocus(focusable);
            }
        }
        if (removed && sceneAttachedModals.remove(modal)) {
            root.removeChild(modal);
        }
        if (removed) {
            markDirty();
        }
        return removed;
    }

    public UiNode hitTest(float x, float y) {
        UiNode modal = getActiveModal();
        if (modal != null) {
            UiNode hit = modal.hitTest(x, y);
            return hit == null ? modal : hit;
        }
        return root.hitTest(x, y);
    }

    @Override
    public boolean dispatch(UiInputEvent event) {
        Objects.requireNonNull(event, "event");
        UiNode target = targetFor(event);
        if (target == null) {
            return false;
        }
        if (event.getType() == UiEventType.POINTER_DOWN && target.isFocusable()) {
            focusManager.requestFocus(target);
        }
        return eventDispatcher.dispatch(event, target);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void onWindowResize(int width, int height) {
        setViewport(new UiViewport(0, 0, width, height, width, height));
    }

    @Override
    public void calculate(float delta) {
        LayoutEngine.layout(root, viewport.uiWidth(), viewport.uiHeight());
        dirty = true;
    }

    @Override
    public void preRender() {
        if (!dirty) {
            return;
        }
        List<DrawCommand> commands = new ArrayList<>();
        Set<UiNode> modalNodes = new LinkedHashSet<>(modals);
        root.collectDrawCommands(commands, modalNodes);
        for (UiNode modal : modals) {
            modal.collectDrawCommands(commands, Set.of());
        }
        drawCommands = List.copyOf(commands);
        clearDirty();
    }

    @Override
    public void render() {
        // A frontend consumes getDrawCommands() on its render thread.
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void clearDirty() {
        dirty = false;
    }

    private UiNode targetFor(UiInputEvent event) {
        if (event.getPosition() != null) {
            return hitTest(event.getPosition().x(), event.getPosition().y());
        }
        UiNode activeModal = getActiveModal();
        UiNode focused = focusManager.getFocusedNode();
        if (activeModal != null) {
            return focused != null && focused.isDescendantOf(activeModal) ? focused : activeModal;
        }
        return focused != null ? focused : root;
    }

    private UiNode firstFocusable(UiNode node) {
        if (node.isFocusable() && node.isEnabled() && node.isVisible()) {
            return node;
        }
        for (UiNode child : node.orderedChildren()) {
            UiNode result = firstFocusable(child);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
