package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.game.core.config.Props;
import io.github.s3s3l.yggdrasil.game.core.object.GameObject;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectManager;
import io.github.s3s3l.yggdrasil.game.core.runtime.GameRuntime;
import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.core.ui.UiColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UiFrontendDrawTelemetry;
import io.github.s3s3l.yggdrasil.game.core.ui.UiNode;
import io.github.s3s3l.yggdrasil.game.core.ui.UiRect;
import io.github.s3s3l.yggdrasil.game.core.ui.UiScene;
import io.github.s3s3l.yggdrasil.game.core.ui.UiViewport;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.FillRectCommand;
import io.github.s3s3l.yggdrasil.game.core.object.props.Visible;

/**
 * Repeatable retained-scene benchmark for OFF, BASIC, and DETAILED telemetry.
 *
 * <p>Run after {@code mvn -pl core test-compile} with:
 * {@code java -cp core/target/test-classes:core/target/classes
 * io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryFrameBenchmark}.
 * The configurable properties {@code ygg.telemetry.benchmark.warmup},
 * {@code ygg.telemetry.benchmark.frames}, and
 * {@code ygg.telemetry.benchmark.repetitions} make CI runs deterministic in
 * amount of work, while the reported wall-clock result remains machine-specific.</p>
 */
public final class TelemetryFrameBenchmark {

    private static final int DRAW_COMMANDS = 128;
    private static final int DEFAULT_WARMUP = 20_000;
    private static final int DEFAULT_FRAMES = 60_000;
    private static final int DEFAULT_REPETITIONS = 5;
    static final String SNAPSHOT_OUTPUT_PROPERTY = "ygg.telemetry.benchmark.snapshotOutput";
    private static volatile long blackhole;

    private TelemetryFrameBenchmark() {
    }

    public static void main(String[] args) {
        int warmup = positiveProperty("ygg.telemetry.benchmark.warmup", DEFAULT_WARMUP);
        int frames = positiveProperty("ygg.telemetry.benchmark.frames", DEFAULT_FRAMES);
        int repetitions = positiveProperty(
                "ygg.telemetry.benchmark.repetitions", DEFAULT_REPETITIONS);

        for (TelemetryLevel level : new TelemetryLevel[] {
                TelemetryLevel.OFF, TelemetryLevel.BASIC, TelemetryLevel.DETAILED }) {
            run(level, warmup);
            long[] samples = new long[repetitions];
            for (int repetition = 0; repetition < repetitions; repetition++) {
                samples[repetition] = run(level, frames);
            }
            Arrays.sort(samples);
            long median = samples[samples.length / 2];
            System.out.printf("%s median=%d ns/frame (%d frames, %d submitted commands/frame)%n",
                    level, median / frames, frames, DRAW_COMMANDS);
        }
        System.out.println("blackhole=" + blackhole);

        String snapshotOutput = System.getProperty(SNAPSHOT_OUTPUT_PROPERTY, "").trim();
        if (!snapshotOutput.isEmpty()) {
            writeSnapshot(Path.of(snapshotOutput), runDiagnosticWorkload());
        }
    }

    private static long run(TelemetryLevel level, int frames) {
        GameRuntime runtime = new GameRuntime(config(level));
        try {
            UiScene scene = new UiScene(sceneRoot(), new UiViewport(0, 0, 1024, 768, 1024, 768),
                    runtime.telemetry());
            UiFrontendDrawTelemetry frontend = new UiFrontendDrawTelemetry(runtime.telemetry());
            Renderable frame = new SceneFrame(scene, frontend);
            long started = System.nanoTime();
            for (int index = 0; index < frames; index++) {
                runtime.runFrame(frame, 0f);
            }
            long elapsed = System.nanoTime() - started;
            blackhole ^= runtime.telemetry().snapshot().cumulative()
                    .uiFrontendDrawCommandsSubmitted();
            return elapsed;
        } finally {
            runtime.close();
        }
    }

    private static TelemetryConfig config(TelemetryLevel level) {
        return new TelemetryConfig(level, Long.MAX_VALUE / 1_000_000L, "none", "",
                false, Set.of(), 10, 0L);
    }

    /**
     * Exercises telemetry-bearing APIs after timing has completed. This is not
     * representative application data; it makes the exported diagnostic
     * snapshot prove that the object, UI, frontend, and resource paths work.
     */
    static TelemetrySnapshot runDiagnosticWorkload() {
        GameRuntime runtime = new GameRuntime(new TelemetryConfig(TelemetryLevel.DETAILED,
                Long.MAX_VALUE / 1_000_000L, "none", "", true, Set.of("true"), 10, 0L));
        TelemetryRegistry telemetry = runtime.telemetry();
        try {
            GameObjectManager objects = runtime.resources().register(
                    new GameObjectManager(telemetry));
            runtime.resources().register((AutoCloseable) () -> {
            });
            DiagnosticObject object = objects.create(
                    new GameObjectConfig(DiagnosticObject.class, new Props()), DiagnosticObject.class);
            UiScene scene = new UiScene(diagnosticSceneRoot(),
                    new UiViewport(0, 0, 64, 48, 64, 48), telemetry);
            DiagnosticFrame frame = new DiagnosticFrame(objects, object, scene,
                    new UiFrontendDrawTelemetry(telemetry));

            runtime.runFrame(frame, 0f);
            runtime.runFrame(frame, 0f);
            scene.markDirty();
            frame.releaseAfterRender();
            runtime.runFrame(frame, 0f);
        } finally {
            runtime.close();
        }
        return telemetry.snapshot();
    }

    static void writeSnapshot(Path output, TelemetrySnapshot snapshot) {
        Path absolute = output.toAbsolutePath().normalize();
        try {
            Path parent = absolute.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(absolute, TelemetryJson.toJson(snapshot, Instant.now().toString()),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException failure) {
            throw new UncheckedIOException("Cannot write telemetry benchmark snapshot: " + absolute,
                    failure);
        }
    }

    private static UiNode sceneRoot() {
        UiNode root = new UiNode();
        for (int index = 0; index < DRAW_COMMANDS; index++) {
            int offset = index;
            UiNode node = new UiNode();
            node.setDrawHandler((ignored, context) -> context.add(new FillRectCommand(
                    new UiRect(offset, offset, 1, 1), new UiColor(1, 1, 1, 1))));
            root.addChild(node);
        }
        return root;
    }

    private static UiNode diagnosticSceneRoot() {
        UiNode root = new UiNode();
        UiNode child = new UiNode();
        child.setDrawHandler((ignored, context) -> context.add(new FillRectCommand(
                new UiRect(0, 0, 8, 8), new UiColor(1, 1, 1, 1))));
        root.addChild(child);
        return root;
    }

    private static int positiveProperty(String name, int fallback) {
        try {
            int parsed = Integer.parseInt(System.getProperty(name, Integer.toString(fallback)));
            return parsed > 0 ? parsed : fallback;
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static final class SceneFrame implements Renderable {

        private final UiScene scene;
        private final UiFrontendDrawTelemetry frontend;

        private SceneFrame(UiScene scene, UiFrontendDrawTelemetry frontend) {
            this.scene = scene;
            this.frontend = frontend;
        }

        @Override
        public long getId() {
            return scene.getId();
        }

        @Override
        public void setId(long id) {
            scene.setId(id);
        }

        @Override
        public void onWindowResize(int width, int height) {
            scene.onWindowResize(width, height);
        }

        @Override
        public void calculate(float delta) {
            scene.calculate(delta);
        }

        @Override
        public void preRender() {
            scene.preRender();
        }

        @Override
        public void render() {
            long submitted = 0L;
            for (Object ignored : scene.getDrawCommands()) {
                submitted++;
            }
            frontend.recordSubmittedDrawCommands(submitted);
            frontend.recordSubmittedDrawNodes(DRAW_COMMANDS);
            frontend.recordSubmittedDrawBatches(1L);
            blackhole += submitted;
        }

        @Override
        public boolean isDirty() {
            return scene.isDirty();
        }

        @Override
        public void markDirty() {
            scene.markDirty();
        }

        @Override
        public void clearDirty() {
            scene.clearDirty();
        }
    }

    private static final class DiagnosticFrame implements Renderable {

            private final GameObjectManager objects;
            private final DiagnosticObject object;
            private final UiScene scene;
            private final UiFrontendDrawTelemetry frontend;
            private boolean releaseAfterRender;

            private DiagnosticFrame(GameObjectManager objects, DiagnosticObject object, UiScene scene,
                    UiFrontendDrawTelemetry frontend) {
                this.objects = objects;
                this.object = object;
                this.scene = scene;
                this.frontend = frontend;
            }

            private void releaseAfterRender() {
                releaseAfterRender = true;
            }

            @Override
            public long getId() {
                return scene.getId();
            }

            @Override
            public void setId(long id) {
                scene.setId(id);
            }

            @Override
            public void onWindowResize(int width, int height) {
                scene.onWindowResize(width, height);
            }

            @Override
            public void calculate(float delta) {
                objects.doCalculate(delta);
                scene.calculate(delta);
            }

            @Override
            public void preRender() {
                objects.doPreRender();
                scene.preRender();
            }

            @Override
            public void render() {
                objects.doRender();
                scene.render();
                long commands = scene.getDrawCommands().size();
                frontend.recordSubmittedDrawCommands(commands);
                frontend.recordSubmittedDrawNodes(2L);
                frontend.recordSubmittedDrawBatches(1L);
                if (releaseAfterRender) {
                    objects.release(object);
                    objects.active(object);
                    objects.release(object);
                    objects.reclaimReleasedObjects();
                }
            }

            @Override
            public boolean isDirty() {
                return scene.isDirty();
            }

            @Override
            public void markDirty() {
                scene.markDirty();
            }

            @Override
            public void clearDirty() {
                scene.clearDirty();
            }
        }

        public static final class DiagnosticObject implements GameObject {

            private long id = 1L;
            private final Set<GameObject> children = new HashSet<>();
            private GameObject parent;
            private Visible visible = Visible.SHOW;
            private boolean enabled = true;
            private boolean dirty;

            public DiagnosticObject(GameObjectConfig config) {
            }

            @Override
            public void afterPropertiesSet() {
            }

            @Override
            public void onWindowResize(int width, int height) {
            }

            @Override
            public ObjectSnapshot snapshot() {
                return new ObjectSnapshot(id, DiagnosticObject.class);
            }

            @Override
            public void restore(ObjectSnapshot snapshot) {
                id = snapshot.getId();
            }

            @Override
            public void calculate(float delta) {
                markDirty();
            }

            @Override
            public void preRender() {
            }

            @Override
            public void render() {
                clearDirty();
            }

            @Override
            public long getId() {
                return id;
            }

            @Override
            public void setId(long id) {
                this.id = id;
            }

            @Override
            public boolean isDirty() {
                return dirty;
            }

            @Override
            public void markDirty() {
                dirty = true;
            }

            @Override
            public void clearDirty() {
                dirty = false;
            }

            @Override
            public void finalizeResource() {
            }

            @Override
            public Visible getVisible() {
                return visible;
            }

            @Override
            public void setVisible(Visible visible) {
                this.visible = visible;
            }

            @Override
            public boolean isEnable() {
                return enabled;
            }

            @Override
            public void setEnable(boolean enabled) {
                this.enabled = enabled;
            }

            @Override
            public GameObject parent() {
                return parent;
            }

            @Override
            public void setParent(GameObject parent) {
                this.parent = parent;
            }

            @Override
            public Set<GameObject> children() {
                return children;
            }

            @Override
            public void addChild(GameObject child) {
                children.add(child);
            }
        }
}
