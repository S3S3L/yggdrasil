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

| Property | Default | Effect |
| --- | --- | --- |
| `ygg.telemetry` | `off` | Enables the in-memory frame registry |
| `ygg.telemetry.interval` | `1000` | Milliseconds between completed-frame publications (not a frame count); the first completed frame is published immediately |
| `ygg.telemetry.sink` | `none` | Comma-separated `log`, `json`, and/or `jmx` output sinks |
| `ygg.telemetry.output` | empty | Required JSON Lines file path when `json` is selected; it is rejected for other sinks |
| `ygg.telemetry.jvm` | `false` | Adds cached, low-frequency heap/GC/thread management-bean values |
| `ygg.telemetry.types` | empty | Enables bounded class counters only as `true` at `detailed`/`trace`; `true,fully.qualified.Type` filters them |
| `ygg.telemetry.topN` | `10` | Maximum accepted class IDs retained by bounded type counters |
| `ygg.telemetry.slowFrameMs` | `0` | Marks frames at or over the positive threshold slow |

`GameRuntime.telemetry()` exposes the frame-confined registry and
`TelemetryCollector` SPI. A runtime owns one bounded asynchronous publisher:
the render thread only copies a completed fixed-schema snapshot into a bounded
queue. Its single writer thread performs logging, JSON formatting/file I/O, and
JMX registration. A full queue drops that publication without blocking the
frame and exposes `droppedPublications`; output failures are counted in
`outputFailures` and sent to the configured `TelemetryErrorHandler` (stderr by
default). Closing the runtime drains for a bounded time and closes that writer
through its `ResourceScope`; repeated closes are safe. With telemetry off no
writer or JVM sampler is created, and an enabled registry with `sink=none`
still provides programmatic snapshots.

JSON Lines uses schema version `1` with stable top-level `timestamp`, `frame`,
`cumulative`, `gauges`, `types`, `jvm`, `droppedPublications`, and
`outputFailures` fields. `frame` includes end-to-end duration, completed/slow
state, callbacks/calculate/pre-render/before-render/render phase durations,
and per-frame counters. JVM values unavailable from a VM are represented as
JSON `null`, never as a misleading zero. The JMX sink registers the read-only
`io.github.s3s3l.yggdrasil.game.core.telemetry:type=RuntimeTelemetry` MXBean;
it exposes aggregate fields only, never per-object IDs, and safely unregisters
only the bean it registered.

### Metric semantics

All `TelemetryCounters` are monotonic cumulatives plus the delta recorded
between `beginFrame()` and `endFrame()`. Lifecycle work outside a runtime frame
only changes the cumulative value. Gauges are current values, not frame
deltas.

| Area | Counters | Gauges / notes |
| --- | --- | --- |
| Game objects | `objectsCreated`, `objectsActivated`, `objectsReleased`, `objectsReclaimed`, `objectsCalculated`, `objectsPreRendered`, `objectsRendered`, `objectsDirtyMarked`, `dirtyObjects`, `objectDirtyRoots`, `objectsDirtyPreRendered` | `activeObjectCount`, `releasedRetainedObjectCount`, `objectCount` (their sum), `dirtyObjectCount`, `dirtyRootCount`. `objectsDirtyMarked` is a clean-to-dirty transition observed around manager calculation. Calculate/pre-render/render count manager-dispatched candidates or roots; child recursion remains application behavior and is not counted a second time. |
| Retained UI | `uiLayoutPasses` / `uiLayoutSkips`, `uiDrawSnapshotBuilds` / `uiDrawSnapshotSkips`, `uiCommandSnapshotNodeVisits`, `uiHitTestNodeVisits`, `uiDrawCommandsBuilt`, `uiChildOrderCacheBuilds` | `uiSceneCount`, `uiNodeCount`, `uiDirtySceneCount`, `uiDirtyNodeCount`. Dirty-node gauges express the scene's retained-snapshot invalidation scope. Command-snapshot visits and commands built are **not** frontend draw calls; frontends own actual command execution. |
| Resources | `resourcesRegistered`, `resourcesClosed`, `resourcesCloseFailed` | `resourceCount` is currently active registrations. The legacy `resourcesAcquired`/`resourcesReleased` fields mirror successful registrations/closes. Re-closing a scope does not increment counters. |

Released game objects stay as soft references. A `ReferenceQueue` is polled on
normal manager work and by `reclaimReleasedObjects()`; only the exact retained
reference is removed and counted as `objectsReclaimed`. JVM reclamation is
asynchronous and non-deterministic; telemetry never requests `System.gc()`.
The manager does not invoke `GameObject.finalizeResource()` on release or close,
so it intentionally exposes no `objectsFinalized` counter.

Type counters are enabled only with `-Dygg.telemetry=detailed` (or `trace`) and
`-Dygg.telemetry.types=true`. Class IDs are cached with `ClassValue`, and at
most `topN` first-seen accepted types are retained; this bounded sink SPI is not
a ranked top-N query and does not allocate a type-name map per frame.

## Publishing

See [`doc/publishing.md`](doc/publishing.md) for local verification and remote
repository release steps.
