package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.List;
import java.util.Objects;

import io.github.s3s3l.yggdrasil.game.core.ui.draw.DrawCommand;

/**
 * Collects commands produced while a UI node is traversed.
 */
public final class UiDrawContext {

    private final List<DrawCommand> commands;

    UiDrawContext(List<DrawCommand> commands) {
        this.commands = commands;
    }

    public void add(DrawCommand command) {
        commands.add(Objects.requireNonNull(command, "command"));
    }
}
