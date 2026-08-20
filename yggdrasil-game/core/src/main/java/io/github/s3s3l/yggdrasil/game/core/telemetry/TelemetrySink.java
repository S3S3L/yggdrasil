package io.github.s3s3l.yggdrasil.game.core.telemetry;

interface TelemetrySink extends AutoCloseable {

    void accept(TelemetrySnapshot snapshot) throws Exception;

    @Override
    default void close() throws Exception {
    }
}
