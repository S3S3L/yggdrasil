# Artifact architecture and dependency boundaries

The root `yggdrasil-game` POM is the published parent and aggregation POM. It
has no external parent POM, declares Java 21 and LibGDX 1.12.1 once, and is
installed/deployed together with the child artifacts. `yggdrasil-game-bom`
exports the same versions for Maven and Gradle consumers.

```text
core  <---  2d  <---  gdx  <---  gdx-lwjgl3
  ^                |               |
  |                |               +-- LWJGL3 and desktop natives
  |                +-- LibGDX API and FreeType API only
  +-- runtime, layout, UI protocol, font SPI
```

`core` owns backend-neutral state and APIs. Its public UI values are draw
commands and geometry; its font layer exposes `FontFallbackResolver` without a
platform or FreeType dependency. `2d` builds only on `core` and registers its
finite built-in collision-handler set explicitly. This makes handler
availability deterministic in shaded, native-image, and modular class paths.

`gdx` is the LibGDX adapter. It may use the LibGDX and `gdx-freetype` Java APIs
but does not depend on `gdx-backend-lwjgl3`, `gdx-platform`, or
`gdx-freetype-platform`. A host can therefore choose another backend without
getting desktop natives transitively.

`gdx-lwjgl3` is the only desktop-specific artifact. It contains
`Lwjgl3Launcher` and brings in the LWJGL3 backend and desktop FreeType/LibGDX
natives at runtime. Applications select it deliberately.

## SPI loading

`FontFallbackResolver` providers use Java `ServiceLoader` through
`FontFallbackResolvers`. The loader captures `ServiceConfigurationError` and
linkage failures for each provider, retaining the providers that loaded
successfully. Provider loading is opt-in: callers decide whether to compose
the discovered resolvers into a `FontFallbackPolicy`, preserving the default
deterministic fallback behavior.

The core test suite includes one valid and one intentionally broken provider to
verify that failure isolation. This is distinct from the 2D collision registry,
which intentionally has an explicit fixed registration list rather than
discovering arbitrary classes on the runtime classpath.
