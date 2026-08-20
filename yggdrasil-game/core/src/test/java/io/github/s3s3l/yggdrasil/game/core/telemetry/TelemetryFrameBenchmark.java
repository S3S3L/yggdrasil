package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.Arrays;
import java.util.Set;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.game.core.runtime.GameRuntime;
import io.github.s3s3l.yggdrasil.game.core.ui.UiColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UiFrontendDrawTelemetry;
import io.github.s3s3l.yggdrasil.game.core.ui.UiNode;
import io.github.s3s3l.yggdrasil.game.core.ui.UiRect;
import io.github.s3s3l.yggdrasil.game.core.ui.UiScene;
import io.github.s3s3l.yggdrasil.game.core.ui.UiViewport;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.FillRectCommand;

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
}
