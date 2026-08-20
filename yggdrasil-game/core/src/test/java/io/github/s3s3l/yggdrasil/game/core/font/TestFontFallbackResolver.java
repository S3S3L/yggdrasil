package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.Optional;

public final class TestFontFallbackResolver implements FontFallbackResolver {

    @Override
    public Optional<FontSource> resolve(FontRequest request, int codePoint) {
        return codePoint == 0x2603
                ? Optional.of(new FontSource("test-provider", "test-provider.ttf", 0))
                : Optional.empty();
    }
}
