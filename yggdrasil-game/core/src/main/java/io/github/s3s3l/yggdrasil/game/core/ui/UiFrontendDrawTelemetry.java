package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.Objects;

import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryMetric;
import io.github.s3s3l.yggdrasil.game.core.telemetry.TelemetryRegistry;

/**
 * Reports work actually submitted by a frontend painter.
 *
 * <p>These counters are intentionally separate from retained-scene command
 * snapshot construction. A frontend calls them only after it submits its own
 * commands, nodes, or batches to its painter.</p>
 */
public final class UiFrontendDrawTelemetry {

    private final TelemetryRegistry telemetry;
    private final boolean enabled;

    public UiFrontendDrawTelemetry(TelemetryRegistry telemetry) {
        this.telemetry = Objects.requireNonNull(telemetry, "telemetry");
        this.enabled = telemetry.isEnabled();
    }

    /** Records frontend draw commands submitted to the host painter. */
    public void recordSubmittedDrawCommands(long count) {
        record(TelemetryMetric.UI_FRONTEND_DRAW_COMMANDS_SUBMITTED, count);
    }

    /** Records frontend scene nodes represented by submitted draw work. */
    public void recordSubmittedDrawNodes(long count) {
        record(TelemetryMetric.UI_FRONTEND_DRAW_NODES_SUBMITTED, count);
    }

    /** Records frontend painter batches submitted to the host backend. */
    public void recordSubmittedDrawBatches(long count) {
        record(TelemetryMetric.UI_FRONTEND_DRAW_BATCHES_SUBMITTED, count);
    }

    private void record(TelemetryMetric metric, long count) {
        if (count < 0L) {
            throw new IllegalArgumentException("count must not be negative");
        }
        if (enabled && count > 0L) {
            telemetry.add(metric, count);
        }
    }
}
