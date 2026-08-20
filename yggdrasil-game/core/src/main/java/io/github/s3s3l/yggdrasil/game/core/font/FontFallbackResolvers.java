package io.github.s3s3l.yggdrasil.game.core.font;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * Loads optional {@link FontFallbackResolver} implementations without making a
 * broken third-party provider prevent the core runtime from starting.
 */
public final class FontFallbackResolvers {

    private FontFallbackResolvers() {
    }

    public static LoadResult loadInstalled() {
        return loadInstalled(Thread.currentThread().getContextClassLoader());
    }

    public static LoadResult loadInstalled(ClassLoader classLoader) {
        Objects.requireNonNull(classLoader, "classLoader");
        ServiceLoader<FontFallbackResolver> loader = ServiceLoader.load(FontFallbackResolver.class, classLoader);
        Iterator<ServiceLoader.Provider<FontFallbackResolver>> providers = loader.stream().iterator();
        List<FontFallbackResolver> loaded = new ArrayList<>();
        List<ProviderFailure> failures = new ArrayList<>();

        while (true) {
            ServiceLoader.Provider<FontFallbackResolver> provider;
            try {
                if (!providers.hasNext()) {
                    break;
                }
                provider = providers.next();
            } catch (ServiceConfigurationError | LinkageError error) {
                failures.add(ProviderFailure.from("unknown", error));
                continue;
            }

            String providerName = "unknown";
            try {
                providerName = provider.type().getName();
                loaded.add(provider.get());
            } catch (ServiceConfigurationError | LinkageError error) {
                failures.add(ProviderFailure.from(providerName, error));
            }
        }
        return new LoadResult(loaded, failures);
    }

    public static FontFallbackResolver firstAvailable(List<? extends FontFallbackResolver> resolvers) {
        List<FontFallbackResolver> copy = List.copyOf(resolvers);
        return new FontFallbackResolver() {
            @Override
            public Optional<FontSource> resolve(FontRequest request, int codePoint) {
                for (FontFallbackResolver resolver : copy) {
                    Optional<FontSource> result = resolver.resolve(request, codePoint);
                    if (result.isPresent()) {
                        return result;
                    }
                }
                return Optional.empty();
            }

            @Override
            public void close() {
                RuntimeException failure = null;
                for (FontFallbackResolver resolver : copy) {
                    try {
                        resolver.close();
                    } catch (Exception exception) {
                        if (failure == null) {
                            failure = new IllegalStateException("Failed to close font fallback resolver", exception);
                        } else {
                            failure.addSuppressed(exception);
                        }
                    }
                }
                if (failure != null) {
                    throw failure;
                }
            }
        };
    }

    public record LoadResult(List<FontFallbackResolver> resolvers, List<ProviderFailure> failures) {
        public LoadResult {
            resolvers = List.copyOf(resolvers);
            failures = List.copyOf(failures);
        }
    }

    public record ProviderFailure(String providerName, String message) {
        private static ProviderFailure from(String providerName, Error error) {
            return new ProviderFailure(providerName, error.getMessage());
        }
    }
}
