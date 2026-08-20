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
import io.github.s3s3l.yggdrasil.game.core.telemetry.NoOpTelemetryRegistry;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryGauge;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryMetric;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;
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
    private final TelemetryRegistry telemetry;
    private final boolean telemetryEnabled;
    private final List<UiNode> modals = new ArrayList<>();
    private final Map<UiNode, UiNode> modalPreviousFocus = new HashMap<>();
    private final Set<UiNode> sceneAttachedModals = new LinkedHashSet<>();
    private UiViewport viewport;
    private List<DrawCommand> drawCommands = List.of();
    private boolean dirty = true;
    private boolean layoutDirty = true;
    private long layoutFingerprint = Long.MIN_VALUE;
    private long drawFingerprint = Long.MIN_VALUE;
    private long layoutPasses;
    private long commandSnapshotBuilds;
    private long drawnNodeVisits;
    private long hitTestCalls;
    private long hitTestNodeVisits;
    private long childOrderCacheBuilds;
    private int nodeCount;
    private final UiNode.TraversalObserver traversalObserver = new UiNode.TraversalObserver() {
        @Override
        public void onDrawNodeVisit() {
            drawnNodeVisits++;
        }

        @Override
        public void onHitTestNodeVisit() {
            hitTestNodeVisits++;
        }

        @Override
        public void onChildOrderCacheBuild() {
            childOrderCacheBuilds++;
        }
    };

    public UiScene(UiNode root, UiViewport viewport) {
        this(root, viewport, ignored -> {
        }, NoOpTelemetryRegistry.INSTANCE);
    }

    public UiScene(UiNode root, UiViewport viewport, Consumer<Throwable> eventFailureHandler) {
        this(root, viewport, eventFailureHandler, NoOpTelemetryRegistry.INSTANCE);
    }

    /**
     * Creates a scene that publishes retained-scene work to a runtime registry.
     */
    public UiScene(UiNode root, UiViewport viewport, TelemetryRegistry telemetry) {
        this(root, viewport, ignored -> {
        }, telemetry);
    }

    public UiScene(UiNode root, UiViewport viewport, Consumer<Throwable> eventFailureHandler,
            TelemetryRegistry telemetry) {
        this.root = Objects.requireNonNull(root, "root");
        this.viewport = Objects.requireNonNull(viewport, "viewport");
        this.eventDispatcher = new UiEventDispatcher(eventFailureHandler);
        this.telemetry = Objects.requireNonNull(telemetry, "telemetry");
        this.telemetryEnabled = telemetry.isEnabled();
        if (telemetryEnabled) {
            nodeCount = root.attachToScene(this);
            updateGauges();
        }
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
        UiViewport next = Objects.requireNonNull(viewport, "viewport");
        if (!next.equals(this.viewport)) {
            this.viewport = next;
            markDirty();
        }
    }

    public List<DrawCommand> getDrawCommands() {
        return drawCommands;
    }

    /**
     * Returns deterministic retained-scene work counters. A scene is
     * thread-confined: read these on the same update/render thread that drives
     * {@link #calculate(float)} and {@link #preRender()}.
     */
    public UiSceneMetrics metrics() {
        return new UiSceneMetrics(layoutPasses, commandSnapshotBuilds, drawnNodeVisits,
                hitTestCalls, hitTestNodeVisits, childOrderCacheBuilds, drawCommands.size());
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
        long nodeVisitsBefore = telemetryEnabled ? hitTestNodeVisits : 0L;
        long childOrderBuildsBefore = telemetryEnabled ? childOrderCacheBuilds : 0L;
        hitTestCalls++;
        UiNode modal = getActiveModal();
        UiNode result;
        if (modal != null) {
            UiNode hit = modal.hitTest(x, y, traversalObserver);
            result = hit == null ? modal : hit;
        } else {
            result = root.hitTest(x, y, traversalObserver);
        }
        if (telemetryEnabled) {
            record(TelemetryMetric.UI_HIT_TEST_NODE_VISITS,
                    hitTestNodeVisits - nodeVisitsBefore);
            record(TelemetryMetric.UI_CHILD_ORDER_CACHE_BUILDS,
                    childOrderCacheBuilds - childOrderBuildsBefore);
        }
        return result;
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
        long nextLayoutFingerprint = root.layoutTreeFingerprint();
        if (!layoutDirty && nextLayoutFingerprint == layoutFingerprint) {
            record(TelemetryMetric.UI_LAYOUT_SKIPS);
            updateGauges();
            return;
        }
        LayoutEngine.layout(root, viewport.uiWidth(), viewport.uiHeight());
        layoutFingerprint = root.layoutTreeFingerprint();
        layoutDirty = false;
        dirty = true;
        layoutPasses++;
        if (telemetryEnabled) {
            record(TelemetryMetric.UI_LAYOUT_PASSES);
        }
        updateGauges();
    }

    @Override
    public void preRender() {
        long nodeVisitsBefore = telemetryEnabled ? drawnNodeVisits : 0L;
        long childOrderBuildsBefore = telemetryEnabled ? childOrderCacheBuilds : 0L;
        long nextDrawFingerprint = root.drawTreeFingerprint();
        if (nextDrawFingerprint != drawFingerprint) {
            dirty = true;
        }
        if (!dirty) {
            record(TelemetryMetric.UI_DRAW_SNAPSHOT_SKIPS);
            updateGauges();
            return;
        }
        List<DrawCommand> commands = new ArrayList<>();
        UiDrawContext context = new UiDrawContext(commands);
        Set<UiNode> modalNodes = new LinkedHashSet<>(modals);
        root.collectDrawCommands(context, modalNodes, traversalObserver);
        for (UiNode modal : modals) {
            modal.collectDrawCommands(context, Set.of(), traversalObserver);
        }
        drawCommands = List.copyOf(commands);
        drawFingerprint = root.drawTreeFingerprint();
        commandSnapshotBuilds++;
        clearDirty();
        if (telemetryEnabled) {
            record(TelemetryMetric.UI_DRAW_SNAPSHOT_BUILDS);
            record(TelemetryMetric.UI_COMMAND_SNAPSHOT_NODE_VISITS,
                    drawnNodeVisits - nodeVisitsBefore);
            record(TelemetryMetric.UI_CHILD_ORDER_CACHE_BUILDS,
                    childOrderCacheBuilds - childOrderBuildsBefore);
            record(TelemetryMetric.UI_DRAW_COMMANDS_BUILT, drawCommands.size());
        }
        updateGauges();
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
        layoutDirty = true;
        updateGauges();
    }

    /**
     * Invalidates retained commands without relaying out. Call this after a
     * draw handler's captured state changes (for example, dynamic text).
     */
    public void invalidateDrawCommands() {
        dirty = true;
        updateGauges();
    }

    @Override
    public void clearDirty() {
        dirty = false;
        updateGauges();
    }

    void nodeAttached(UiNode node) {
        if (telemetryEnabled) {
            nodeCount += node.attachToScene(this);
            updateGauges();
        }
    }

    void nodeDetached(UiNode node) {
        if (telemetryEnabled) {
            nodeCount -= node.detachFromScene(this);
            updateGauges();
        }
    }

    private void record(TelemetryMetric metric) {
        record(metric, 1L);
    }

    private void record(TelemetryMetric metric, long amount) {
        if (telemetryEnabled && amount > 0L) {
            telemetry.add(metric, amount);
        }
    }

    private void updateGauges() {
        if (!telemetryEnabled) {
            return;
        }
        telemetry.set(TelemetryGauge.UI_SCENE_COUNT, 1L);
        telemetry.set(TelemetryGauge.UI_NODE_COUNT, nodeCount);
        telemetry.set(TelemetryGauge.UI_DIRTY_SCENE_COUNT, dirty ? 1L : 0L);
        telemetry.set(TelemetryGauge.UI_DIRTY_NODE_COUNT, dirty ? nodeCount : 0L);
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
