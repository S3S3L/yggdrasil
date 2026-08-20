package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Ordered fallback selection independent of a rendering backend.
 *
 * <p>The default Unicode layers preserve an ASCII primary face, prefer CJK and
 * private-use tiers where appropriate, and use a symbol face only after the
 * primary has declined a normal Unicode symbol.</p>
 */
public final class FontFallbackPolicy implements AutoCloseable {

    private final List<FontFallbackRule> rules;
    private final FontFallbackResolver dynamicResolver;
    private boolean closed;

    private FontFallbackPolicy(List<FontFallbackRule> rules, FontFallbackResolver dynamicResolver) {
        this.rules = List.copyOf(rules);
        this.dynamicResolver = dynamicResolver;
    }

    public static FontFallbackPolicy primaryOnly() {
        return builder().build();
    }

    public static FontFallbackPolicy unicodeLayers(
            FontSource cjk, FontSource symbols, FontSource privateUse) {
        return builder()
                .beforePrimary(FontFallbackPolicy::isCjk, cjk)
                .beforePrimary(FontFallbackPolicy::isPrivateUse, privateUse)
                .afterPrimary(FontFallbackPolicy::isSymbol, symbols)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Produces style- and size-preserving concrete requests. Duplicated sources
     * are intentionally collapsed so a source is never probed twice per glyph.
     */
    public synchronized List<FontRequest> candidates(FontRequest primary, int codePoint) {
        Objects.requireNonNull(primary, "primary");
        TextGlyph.requireCodePoint(codePoint);
        if (closed) {
            throw new IllegalStateException("FontFallbackPolicy is closed");
        }
        LinkedHashSet<FontSource> sources = new LinkedHashSet<>();
        addMatching(sources, primary, codePoint, FontFallbackPriority.BEFORE_PRIMARY);
        sources.add(primary.source());
        addMatching(sources, primary, codePoint, FontFallbackPriority.AFTER_PRIMARY);
        Optional<FontSource> dynamic = dynamicResolver.resolve(primary, codePoint);
        dynamic.ifPresent(sources::add);

        List<FontRequest> requests = new ArrayList<>(sources.size());
        for (FontSource source : sources) {
            requests.add(primary.withSource(source));
        }
        return List.copyOf(requests);
    }

    @Override
    public synchronized void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            dynamicResolver.close();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to close dynamic font fallback resolver", exception);
        }
    }

    private void addMatching(
            LinkedHashSet<FontSource> sources,
            FontRequest primary,
            int codePoint,
            FontFallbackPriority priority) {
        for (FontFallbackRule rule : rules) {
            if (rule.priority() == priority && rule.matches().test(codePoint)) {
                sources.add(rule.source());
            }
        }
    }

    private static boolean isCjk(int codePoint) {
        Character.UnicodeScript script = Character.UnicodeScript.of(codePoint);
        return script == Character.UnicodeScript.HAN
                || script == Character.UnicodeScript.HIRAGANA
                || script == Character.UnicodeScript.KATAKANA
                || script == Character.UnicodeScript.HANGUL
                || script == Character.UnicodeScript.BOPOMOFO;
    }

    private static boolean isPrivateUse(int codePoint) {
        return Character.getType(codePoint) == Character.PRIVATE_USE;
    }

    private static boolean isSymbol(int codePoint) {
        int type = Character.getType(codePoint);
        return type == Character.MATH_SYMBOL
                || type == Character.CURRENCY_SYMBOL
                || type == Character.MODIFIER_SYMBOL
                || type == Character.OTHER_SYMBOL;
    }

    public static final class Builder {

        private final List<FontFallbackRule> rules = new ArrayList<>();
        private FontFallbackResolver dynamicResolver = FontFallbackResolver.none();

        public Builder beforePrimary(java.util.function.IntPredicate matches, FontSource source) {
            return add(matches, source, FontFallbackPriority.BEFORE_PRIMARY);
        }

        public Builder afterPrimary(java.util.function.IntPredicate matches, FontSource source) {
            return add(matches, source, FontFallbackPriority.AFTER_PRIMARY);
        }

        public Builder dynamicResolver(FontFallbackResolver resolver) {
            this.dynamicResolver = Objects.requireNonNull(resolver, "resolver");
            return this;
        }

        public FontFallbackPolicy build() {
            return new FontFallbackPolicy(rules, dynamicResolver);
        }

        private Builder add(
                java.util.function.IntPredicate matches,
                FontSource source,
                FontFallbackPriority priority) {
            rules.add(new FontFallbackRule(matches, source, priority));
            return this;
        }
    }
}
