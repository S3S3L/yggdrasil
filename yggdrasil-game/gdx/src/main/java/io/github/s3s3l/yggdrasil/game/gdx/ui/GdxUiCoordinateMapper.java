package io.github.s3s3l.yggdrasil.game.gdx.ui;

import io.github.s3s3l.yggdrasil.game.core.ui.UiViewport;

/**
 * Converts LibGDX viewport bounds into the coordinate space received by an
 * {@code InputProcessor}.
 *
 * <p>Pass the dimensions in which LibGDX reported the viewport bounds and
 * those in which input callbacks report positions. They may differ on HiDPI
 * platforms; this explicit conversion keeps the core UI independent of GDX's
 * logical-versus-backbuffer policy.</p>
 */
public final class GdxUiCoordinateMapper {

    private GdxUiCoordinateMapper() {
    }

    public static UiViewport fromViewportBounds(float viewportX, float viewportY, float viewportWidth,
            float viewportHeight, float viewportCoordinateWidth, float viewportCoordinateHeight,
            float inputCoordinateWidth, float inputCoordinateHeight, float uiWidth, float uiHeight) {
        if (viewportCoordinateWidth <= 0 || viewportCoordinateHeight <= 0
                || inputCoordinateWidth <= 0 || inputCoordinateHeight <= 0) {
            throw new IllegalArgumentException("coordinate dimensions must be positive");
        }
        float xScale = inputCoordinateWidth / viewportCoordinateWidth;
        float yScale = inputCoordinateHeight / viewportCoordinateHeight;
        return new UiViewport(viewportX * xScale, viewportY * yScale, viewportWidth * xScale,
                viewportHeight * yScale, uiWidth, uiHeight);
    }
}
