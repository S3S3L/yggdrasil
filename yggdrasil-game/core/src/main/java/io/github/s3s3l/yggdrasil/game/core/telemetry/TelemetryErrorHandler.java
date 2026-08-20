package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.util.Objects;

/**
 * Receives asynchronous telemetry output failures on the writer thread.
 */
@FunctionalInterface
public interface TelemetryErrorHandler {

    void onFailure(Throwable failure);

    static TelemetryErrorHandler stderr() {
        return failure -> {
            Objects.requireNonNull(failure, "failure");
            System.err.println("Yggdrasil telemetry output failure: " + failure);
        };
    }
}
