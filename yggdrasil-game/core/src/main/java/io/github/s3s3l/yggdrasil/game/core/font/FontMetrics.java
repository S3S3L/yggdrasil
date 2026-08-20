package io.github.s3s3l.yggdrasil.game.core.font;

/**
 * Face metrics in logical UI units.
 *
 * <p>{@code baseline} is the distance from the layout line top to the
 * baseline. It lets renderers align faces with different ascenders without
 * relying on a backend-specific cap-height convention.</p>
 */
public record FontMetrics(float ascent, float descent, float lineHeight, float baseline) {

    public static final FontMetrics EMPTY = new FontMetrics(0f, 0f, 0f, 0f);

    public FontMetrics {
        if (!Float.isFinite(ascent) || !Float.isFinite(descent)
                || !Float.isFinite(lineHeight) || !Float.isFinite(baseline)
                || lineHeight < 0f) {
            throw new IllegalArgumentException("font metrics must be finite with non-negative lineHeight");
        }
    }
}
