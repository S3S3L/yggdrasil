package io.github.s3s3l.yggdrasil.game.core.ui.draw;

import java.util.Objects;

import io.github.s3s3l.yggdrasil.game.core.ui.UiRect;

public final class PushClipCommand implements DrawCommand {

    private final UiRect bounds;

    public PushClipCommand(UiRect bounds) {
        this.bounds = Objects.requireNonNull(bounds, "bounds");
    }

    public UiRect getBounds() {
        return bounds;
    }
}
