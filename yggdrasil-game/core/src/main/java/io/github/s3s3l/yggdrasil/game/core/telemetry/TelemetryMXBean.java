package io.github.s3s3l.yggdrasil.game.core.telemetry;

/**
 * Read-only JMX view. A negative JVM value means that the field is unavailable.
 */
public interface TelemetryMXBean {

    int getSchemaVersion();

    long getFrameNumber();

    long getFrameDurationNanos();

    boolean isFrameCompleted();

    boolean isSlowFrame();

    long getDroppedPublications();

    long getOutputFailures();

    boolean isJvmSampleAvailable();

    long getHeapUsedBytes();

    long getHeapCommittedBytes();

    long getHeapMaxBytes();

    long getGcCount();

    long getGcTimeMillis();

    long getLiveThreadCount();

    long getPeakThreadCount();
}
