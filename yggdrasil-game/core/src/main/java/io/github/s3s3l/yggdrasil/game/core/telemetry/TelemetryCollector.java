package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Service-provider hook for consuming completed telemetry snapshots.
 *
 * <p>No collectors are installed by default. Implementations should return
 * quickly; the runtime invokes collectors synchronously at frame end.</p>
 */
@FunctionalInterface
public interface TelemetryCollector {

    void collect(TelemetrySnapshot snapshot);
}
