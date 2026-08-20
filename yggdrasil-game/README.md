# Yggdrasil Game

Yggdrasil Game is a Java 21 game runtime with backend-neutral core, layout,
UI, and font APIs. LibGDX and LWJGL3 are opt-in adapters rather than
requirements of the core artifacts.

## Modules and compatibility

| Artifact | Java | Purpose | Desktop backend or native dependency |
| --- | --- | --- | --- |
| `yggdrasil-game-core` | 21 | Runtime, layout, UI draw protocol, font SPI | No |
| `yggdrasil-game-2d` | 21 | Shapes and collision handlers | No |
| `yggdrasil-game-gdx` | 21 | LibGDX UI, rendering, and FreeType adapter | No LWJGL3 or platform-native dependency |
| `yggdrasil-game-gdx-lwjgl3` | 21 | Optional LWJGL3 launcher | Yes; explicitly selected |

All modules use LibGDX `1.12.1`. The `gdx` adapter can be paired with a
different LibGDX backend; choose `gdx-lwjgl3` only for the desktop backend and
its native runtime artifacts.

## Gradle consumption

After publishing or running `mvn install`, consume the BOM and only the
artifacts that the application needs:

```groovy
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation platform("io.github.s3s3l:yggdrasil-game-bom:3.8.0-SNAPSHOT")
    implementation "io.github.s3s3l:yggdrasil-game-core"
    implementation "io.github.s3s3l:yggdrasil-game-gdx"

    // Opt in only when launching with LibGDX's LWJGL3 desktop backend.
    implementation "io.github.s3s3l:yggdrasil-game-gdx-lwjgl3"
}
```

The executable Gradle Java 21 consumer fixture is in
[`integration/gradle-consumer`](integration/gradle-consumer). Its `check` task
proves that core plus the GDX adapter resolves without LWJGL3 or desktop-native
artifacts.

## Extension points

2D collision handlers are explicitly registered by the 2D module; no runtime
classpath scanner is used. Platform font fallback remains optional through
`FontFallbackResolver`. Applications can discover providers safely:

```java
var providers = FontFallbackResolvers.loadInstalled();
var resolver = FontFallbackResolvers.firstAvailable(providers.resolvers());
var policy = FontFallbackPolicy.builder().dynamicResolver(resolver).build();
```

`loadInstalled()` retains working providers and reports malformed or failing
providers in `failures()` instead of preventing startup.

## Telemetry

Telemetry is disabled by default and is owned by each `GameRuntime`. Enable its
fixed-schema frame collector at process startup with
`-Dygg.telemetry=basic` (or `detailed` / `trace`). An explicit
`TelemetryConfig` constructor argument takes precedence over system properties.

| Property | Default | Current effect |
| --- | --- | --- |
| `ygg.telemetry` | `off` | Enables the in-memory frame registry |
| `ygg.telemetry.interval` | `1000` | Parsed and retained for a future reporting scheduler |
| `ygg.telemetry.sink` | `none` | Parsed and retained; no built-in sink is installed |
| `ygg.telemetry.output` | empty | Parsed and retained; no output is written |
| `ygg.telemetry.jvm` | `false` | Parsed and retained; JVM collection is not implemented |
| `ygg.telemetry.types` | empty | Comma-separated type filter retained for future instrumentation |
| `ygg.telemetry.topN` | `10` | Parsed and retained for future aggregation |
| `ygg.telemetry.slowFrameMs` | `0` | Marks completed frame snapshots slow when positive |

`GameRuntime.telemetry()` exposes the frame-confined registry and
`TelemetryCollector` SPI. No object/UI/resource instrumentation or log, JSON,
JMX, and JVM sinks are installed in this initial core.

## Publishing

See [`doc/publishing.md`](doc/publishing.md) for local verification and remote
repository release steps.
