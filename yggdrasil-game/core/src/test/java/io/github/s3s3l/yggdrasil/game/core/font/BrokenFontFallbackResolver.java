package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Optional;

public final class BrokenFontFallbackResolver implements FontFallbackResolver {

    public BrokenFontFallbackResolver() {
        throw new IllegalStateException("intentional test provider failure");
    }

    @Override
    public Optional<FontSource> resolve(FontRequest request, int codePoint) {
        return Optional.empty();
    }
}
