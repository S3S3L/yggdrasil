package io.github.s3s3l.yggdrasil.game.core.font;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

class FontSystemTest {

    @Test
    void layeredPolicyRoutesAsciiCjkSymbolsAndPrivateUseInDefinedOrder() {
        FontSource primary = source("primary");
        FontSource cjk = source("cjk");
        FontSource symbols = source("symbols");
        FontSource privateUse = source("private-use");
        FakeProvider provider = new FakeProvider();
        provider.cover(primary, 'A');
        provider.cover(cjk, 0x4E2D);
        provider.cover(cjk, 0x20000);
        provider.cover(symbols, 0x2605);
        provider.cover(privateUse, 0xE0B0);
        FontSystem fonts = new FontSystem(
                FontFallbackPolicy.unicodeLayers(cjk, symbols, privateUse), provider);
        FontHandle handle = handle(primary, 16f);

        assertEquals(primary, text(fonts.resolve(handle, 'A')).request().source());
        assertEquals(cjk, text(fonts.resolve(handle, 0x4E2D)).request().source());
        assertEquals(cjk, text(fonts.resolve(handle, 0x20000)).request().source());
        assertEquals(symbols, text(fonts.resolve(handle, 0x2605)).request().source());
        assertEquals(privateUse, text(fonts.resolve(handle, 0xE0B0)).request().source());

        assertEquals(List.of(
                "primary:U+0041",
                "cjk:U+4E2D",
                "cjk:U+20000",
                "primary:U+2605",
                "symbols:U+2605",
                "private-use:U+E0B0"), provider.coverageCalls);
    }

    @Test
    void missingGlyphsAndAbsentPlatformResolverFallBackWithoutNativeDependency() {
        FontSource primary = source("primary");
        FakeProvider provider = new FakeProvider();
        FontSystem fonts = new FontSystem(FontFallbackPolicy.primaryOnly(), provider);

        FontGlyph glyph = fonts.resolve(handle(primary, 14f), 0x1F642);

        assertInstanceOf(MissingGlyph.class, glyph);
        assertTrue(glyph.isMissing());
        assertEquals(List.of("primary:U+1F642"), provider.coverageCalls);
    }

    @Test
    void preservesMetricsForBaselineAlignmentAndWideOrNarrowAdvances() {
        FontSource primary = source("primary");
        FakeProvider provider = new FakeProvider();
        FontGlyphMetrics narrow = metrics(7f, new FontBounds(-1f, -8f, 8f, 10f), 9f);
        FontGlyphMetrics wide = metrics(18f, new FontBounds(0f, -9f, 17f, 12f), 11f);
        provider.cover(primary, 'i');
        provider.cover(primary, 0x4E2D);
        provider.metrics.put(key(primary, 'i'), narrow);
        provider.metrics.put(key(primary, 0x4E2D), wide);
        FontSystem fonts = new FontSystem(FontFallbackPolicy.primaryOnly(), provider);
        FontHandle handle = handle(primary, 18f);

        FontGlyph measuredNarrow = fonts.resolve(handle, 'i');
        FontGlyph measuredWide = fonts.resolve(handle, 0x4E2D);

        assertEquals(7f, measuredNarrow.metrics().advanceX());
        assertEquals(18f, measuredWide.metrics().advanceX());
        assertEquals(-1f, measuredNarrow.metrics().inkBounds().x());
        assertEquals(9f, measuredNarrow.metrics().fontMetrics().baseline());
        assertEquals(11f, measuredWide.metrics().fontMetrics().baseline());
    }

    @Test
    void cachesCoverageAndMetricsBySourceAndCodePointThenInvalidatesForDpiOrSizeChanges() {
        FontSource primary = source("primary");
        FontSource cjk = source("cjk");
        FakeProvider provider = new FakeProvider();
        provider.cover(cjk, 0x4E2D);
        FontSystem fonts = new FontSystem(
                FontFallbackPolicy.builder().beforePrimary(cp -> cp == 0x4E2D, cjk).build(), provider);
        FontHandle handle = handle(primary, 16f);

        FontGlyph first = fonts.resolve(handle, 0x4E2D);
        FontGlyph second = fonts.resolve(handle, 0x4E2D);

        assertSame(first, second);
        assertEquals(List.of("cjk:U+4E2D"), provider.coverageCalls);
        assertEquals(1, provider.metricCalls.size());
        assertEquals(1, fonts.cachedCoverageCount());
        assertEquals(1, fonts.cachedGlyphCount());
        assertFalse(fonts.updateDpiScale(1f));
        assertTrue(fonts.updateDpiScale(2f));
        assertEquals(List.of(2f), provider.rebuildScales);
        assertEquals(0, fonts.cachedCoverageCount());
        assertEquals(0, fonts.cachedGlyphCount());

        fonts.resolve(handle(primary, 20f), 0x4E2D);
        assertEquals(2, provider.coverageCalls.size());
        assertEquals(2, provider.metricCalls.size());
        fonts.invalidateForFontSizeChange();
        assertEquals(List.of(2f, 2f), provider.rebuildScales);
        assertEquals(0, fonts.cachedGlyphCount());
    }

    @Test
    void imageGlyphsBypassTextCoverageAndAreRebuiltAndClosedByTheSystem() {
        FontSource primary = source("primary");
        FakeProvider provider = new FakeProvider();
        TrackingImageSource images = new TrackingImageSource();
        FontSystem fonts = new FontSystem(FontFallbackPolicy.primaryOnly(), provider, images);

        FontGlyph first = fonts.resolve(handle(primary, 16f), 0x1F600);
        FontGlyph second = fonts.resolve(handle(primary, 16f), 0x1F600);

        assertInstanceOf(ImageGlyph.class, first);
        assertSame(first, second);
        assertTrue(first.isImage());
        assertEquals(1, images.resolveCalls);
        assertTrue(provider.coverageCalls.isEmpty());
        fonts.updateDpiScale(2f);
        assertEquals(List.of(2f), images.rebuildScales);
        fonts.close();
        assertTrue(images.closed);
        assertTrue(provider.closed);
        assertThrows(IllegalStateException.class, () -> fonts.resolve(handle(primary, 16f), 'A'));
    }

    @Test
    void closesDynamicResolverWithTheFontSystem() {
        FontSource primary = source("primary");
        FakeProvider provider = new FakeProvider();
        TrackingResolver resolver = new TrackingResolver();
        FontSystem fonts = new FontSystem(
                FontFallbackPolicy.builder().dynamicResolver(resolver).build(), provider);

        fonts.close();

        assertTrue(resolver.closed);
    }

    private static FontSource source(String id) {
        return new FontSource(id, "fonts/" + id + ".ttf", 0);
    }

    private static FontHandle handle(FontSource source, float size) {
        return new FontHandle(source.id() + "-" + size,
                new FontRequest(source, FontStyle.REGULAR, size));
    }

    private static TextGlyph text(FontGlyph glyph) {
        return assertInstanceOf(TextGlyph.class, glyph);
    }

    private static FontGlyphMetrics metrics(float advance, FontBounds bounds, float baseline) {
        return new FontGlyphMetrics(advance, 0f, bounds,
                new FontMetrics(baseline, -3f, baseline + 3f, baseline));
    }

    private static String key(FontSource source, int codePoint) {
        return source.id() + ":" + codePoint;
    }

    private static String call(FontRequest request, int codePoint) {
        return String.format("%s:U+%04X", request.source().id(), codePoint);
    }

    private static final class FakeProvider implements FontGlyphProvider {

        private final Set<String> coverage = new HashSet<>();
        private final Map<String, FontGlyphMetrics> metrics = new HashMap<>();
        private final List<String> coverageCalls = new ArrayList<>();
        private final List<String> metricCalls = new ArrayList<>();
        private final List<Float> rebuildScales = new ArrayList<>();
        private boolean closed;

        void cover(FontSource source, int codePoint) {
            coverage.add(key(source, codePoint));
        }

        @Override
        public boolean hasGlyph(FontRequest request, int codePoint) {
            coverageCalls.add(call(request, codePoint));
            return coverage.contains(key(request.source(), codePoint));
        }

        @Override
        public FontGlyphMetrics metrics(FontRequest request, int codePoint) {
            metricCalls.add(call(request, codePoint));
            return metrics.getOrDefault(key(request.source(), codePoint),
                    FontGlyphMetrics.EMPTY);
        }

        @Override
        public void rebuild(float dpiScale) {
            rebuildScales.add(dpiScale);
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    private static final class TrackingImageSource implements ImageGlyphSource {

        private int resolveCalls;
        private final List<Float> rebuildScales = new ArrayList<>();
        private boolean closed;

        @Override
        public Optional<ImageGlyph> resolve(FontRequest request, int codePoint) {
            resolveCalls++;
            return Optional.of(new ImageGlyph("emoji-" + codePoint, codePoint,
                    metrics(16f, new FontBounds(0f, -12f, 16f, 16f), 12f)));
        }

        @Override
        public void rebuild(float dpiScale) {
            rebuildScales.add(dpiScale);
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    private static final class TrackingResolver implements FontFallbackResolver {

        private boolean closed;

        @Override
        public Optional<FontSource> resolve(FontRequest request, int codePoint) {
            return Optional.empty();
        }

        @Override
        public void close() {
            closed = true;
        }
    }
}
