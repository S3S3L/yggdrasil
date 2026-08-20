package io.github.s3s3l.yggdrasil.game.gdx.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.layout.LayoutResult;
import io.github.s3s3l.yggdrasil.game.core.ui.UiNode;
import io.github.s3s3l.yggdrasil.game.core.ui.UiPoint;
import io.github.s3s3l.yggdrasil.game.core.ui.UiScene;
import io.github.s3s3l.yggdrasil.game.core.ui.UiViewport;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventPhase;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventType;

class GdxUiInputProcessorTest {

    @Test
    void mapsGdxPointerCoordinatesBeforeDispatchingToCoreUi() {
        UiNode root = new UiNode();
        root.setLayoutResult(new LayoutResult(0, 0, 200, 100, 200, 100));
        AtomicReference<UiPoint> received = new AtomicReference<>();
        root.addEventListener(UiEventType.POINTER_DOWN, UiEventPhase.TARGET, event -> {
            received.set(event.getPosition());
            event.consume();
        });
        UiScene scene = new UiScene(root, new UiViewport(100, 50, 400, 200, 200, 100));
        GdxUiInputProcessor processor = new GdxUiInputProcessor(scene, scene.getViewport());

        assertTrue(processor.touchDown(300, 150, 0, 0));
        assertFalse(processor.touchDown(500, 150, 0, 0));
        assertEquals(new UiPoint(100, 50), received.get());
    }

    @Test
    void scalesViewportBoundsForHiDpiInputCoordinates() {
        UiViewport viewport = GdxUiCoordinateMapper.fromViewportBounds(
                100, 50, 400, 200, 1000, 500, 2000, 1000, 200, 100);

        assertEquals(new UiPoint(100, 50), viewport.toUi(600, 300).orElseThrow());
    }
}
