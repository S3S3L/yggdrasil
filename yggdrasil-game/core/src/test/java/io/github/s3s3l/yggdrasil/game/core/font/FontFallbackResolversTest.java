package io.github.s3s3l.yggdrasil.game.core.font;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FontFallbackResolversTest {

    @Test
    void loadsWorkingProvidersAndIsolatesBrokenProviders() {
        FontFallbackResolvers.LoadResult result = FontFallbackResolvers.loadInstalled();

        assertTrue(result.resolvers().stream()
                .anyMatch(resolver -> resolver instanceof TestFontFallbackResolver));
        assertTrue(result.failures().stream()
                .anyMatch(failure -> failure.providerName().endsWith("BrokenFontFallbackResolver")));

        FontSource expected = new FontSource("test-provider", "test-provider.ttf", 0);
        FontFallbackResolver resolver = FontFallbackResolvers.firstAvailable(result.resolvers());

        assertEquals(expected, resolver.resolve(
                new FontRequest(new FontSource("primary", "primary.ttf", 0), FontStyle.REGULAR, 12f),
                0x2603).orElseThrow());
    }
}
