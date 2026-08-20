package io.github.s3s3l.yggdrasil.game.core.telemetry;

final class LoggingTelemetrySink implements TelemetrySink {

    private static final System.Logger LOGGER = System.getLogger(LoggingTelemetrySink.class.getName());

    @Override
    public void accept(TelemetrySnapshot snapshot) {
        TelemetryFrameSnapshot frame = snapshot.frame();
        LOGGER.log(System.Logger.Level.INFO,
                "frame={0} durationNanos={1} completed={2} slow={3} droppedPublications={4}",
                frame.frameNumber(), frame.durationNanos(), frame.completed(), frame.slow(),
                snapshot.droppedPublications());
    }
}
