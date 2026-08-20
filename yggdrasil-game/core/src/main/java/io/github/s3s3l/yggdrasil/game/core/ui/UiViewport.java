package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.Optional;

/**
 * Maps an input surface rectangle to top-left-origin UI coordinates.
 *
 * <p>All screen values must use the same input coordinate space. On HiDPI
 * backends this may be logical window pixels or framebuffer pixels, but the
 * viewport rectangle and incoming input coordinates must use the same one.
 * The right and bottom screen edges are exclusive.</p>
 */
public record UiViewport(float screenX, float screenY, float screenWidth, float screenHeight,
        float uiWidth, float uiHeight) {

    public UiViewport {
        requirePositive(screenWidth, "screenWidth");
        requirePositive(screenHeight, "screenHeight");
        requirePositive(uiWidth, "uiWidth");
        requirePositive(uiHeight, "uiHeight");
    }

    public Optional<UiPoint> toUi(float inputX, float inputY) {
        if (inputX < screenX || inputX >= screenX + screenWidth
                || inputY < screenY || inputY >= screenY + screenHeight) {
            return Optional.empty();
        }
        return Optional.of(new UiPoint((inputX - screenX) * uiWidth / screenWidth,
                (inputY - screenY) * uiHeight / screenHeight));
    }

    private static void requirePositive(float value, String name) {
        if (Float.isNaN(value) || Float.isInfinite(value) || value <= 0) {
            throw new IllegalArgumentException(name + " must be finite and positive");
        }
    }
}
