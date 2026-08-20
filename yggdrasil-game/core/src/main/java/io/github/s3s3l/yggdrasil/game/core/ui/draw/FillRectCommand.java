package io.github.s3s3l.yggdrasil.game.core.ui.draw;

import java.util.Objects;

import io.github.s3s3l.yggdrasil.game.core.ui.UiColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UiRect;

public final class FillRectCommand implements DrawCommand {

    private final UiRect bounds;
    private final UiColor color;

    public FillRectCommand(UiRect bounds, UiColor color) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
        this.color = Objects.requireNonNull(color, "color");
    }

    public UiRect getBounds() {
        return bounds;
    }

    public UiColor getColor() {
        return color;
    }
}
