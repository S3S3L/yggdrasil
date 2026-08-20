package io.github.s3s3l.yggdrasil.game.core.ui.draw;

/**
 * Restores the clip preceding the matching {@link PushClipCommand}.
 */
public final class PopClipCommand implements DrawCommand {

    public static final PopClipCommand INSTANCE = new PopClipCommand();

    private PopClipCommand() {
    }
}
