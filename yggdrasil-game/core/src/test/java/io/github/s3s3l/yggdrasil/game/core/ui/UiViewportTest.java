package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UiViewportTest {

    @Test
    void mapsViewportCoordinatesWithoutFlippingTopLeftYAxis() {
        UiViewport viewport = new UiViewport(100, 200, 400, 200, 200, 100);

        UiPoint topLeft = viewport.toUi(100, 200).orElseThrow();
        UiPoint center = viewport.toUi(300, 300).orElseThrow();

        assertEquals(new UiPoint(0, 0), topLeft);
        assertEquals(new UiPoint(100, 50), center);
        assertTrue(viewport.toUi(500, 300).isEmpty());
        assertTrue(viewport.toUi(300, 400).isEmpty());
    }

    @Test
    void supportsHiDpiWhenInputAndViewportUseTheSamePhysicalSpace() {
        UiViewport viewport = new UiViewport(200, 100, 800, 400, 200, 100);

        assertEquals(new UiPoint(100, 50), viewport.toUi(600, 300).orElseThrow());
    }
}
