package io.github.s3s3l.yggdrasil.game.core.ui.draw;

import java.util.Objects;

import io.github.s3s3l.yggdrasil.game.core.ui.UiColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UiPoint;

/**
 * Draws text using a baseline in UI coordinates. Font selection is renderer-owned.
 */
public final class TextCommand implements DrawCommand {

    private final String text;
    private final UiPoint baseline;
    private final UiColor color;

    public TextCommand(String text, UiPoint baseline, UiColor color) {
        this.text = Objects.requireNonNull(text, "text");
        this.baseline = Objects.requireNonNull(baseline, "baseline");
        this.color = Objects.requireNonNull(color, "color");
    }

    public String getText() {
        return text;
    }

    public UiPoint getBaseline() {
        return baseline;
    }

    public UiColor getColor() {
        return color;
    }
}
