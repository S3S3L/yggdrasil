package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Objects;
import java.util.function.IntPredicate;

/** One ordered, code-point-specific fallback tier. */
public record FontFallbackRule(
        IntPredicate matches,
        FontSource source,
        FontFallbackPriority priority) {

    public FontFallbackRule {
        Objects.requireNonNull(matches, "matches");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(priority, "priority");
    }
}
