package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.lang.management.ManagementFactory;
import java.util.OptionalLong;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;

final class JmxTelemetrySink implements TelemetrySink {

    static final String OBJECT_NAME_TEXT = "io.github.s3s3l.yggdrasil.game.core.telemetry:type=RuntimeTelemetry";

    private final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    private final ObjectName objectName;
    private final RuntimeTelemetryMBean bean = new RuntimeTelemetryMBean();
    private boolean registered;

    JmxTelemetrySink() {
        try {
            objectName = new ObjectName(OBJECT_NAME_TEXT);
        } catch (Exception invalid) {
            throw new IllegalStateException("Invalid telemetry JMX object name", invalid);
        }
    }

    @Override
    public void accept(TelemetrySnapshot snapshot) throws Exception {
        bean.snapshot = snapshot;
        if (!registered) {
            server.registerMBean(new StandardMBean(bean, TelemetryMXBean.class, true), objectName);
            registered = true;
        }
    }

    @Override
    public void close() throws Exception {
        if (registered) {
            try {
                if (server.isRegistered(objectName)) {
                    server.unregisterMBean(objectName);
                }
            } finally {
                registered = false;
            }
        }
    }

    private static final class RuntimeTelemetryMBean implements TelemetryMXBean {

        private volatile TelemetrySnapshot snapshot = TelemetrySnapshot.DISABLED;

        @Override
        public int getSchemaVersion() {
            return TelemetryJson.SCHEMA_VERSION;
        }

        @Override
        public long getFrameNumber() {
            return snapshot.frame().frameNumber();
        }

        @Override
        public long getFrameDurationNanos() {
            return snapshot.frame().durationNanos();
        }

        @Override
        public boolean isFrameCompleted() {
            return snapshot.frame().completed();
        }

        @Override
        public boolean isSlowFrame() {
            return snapshot.frame().slow();
        }

        @Override
        public long getDroppedPublications() {
            return snapshot.droppedPublications();
        }

        @Override
        public long getOutputFailures() {
            return snapshot.outputFailures();
        }

        @Override
        public boolean isJvmSampleAvailable() {
            return snapshot.jvm().isPresent();
        }

        @Override
        public long getHeapUsedBytes() {
            return jvmValue(snapshot.jvm().map(TelemetryJvmSnapshot::heapUsedBytes)
                    .orElse(OptionalLong.empty()));
        }

        @Override
        public long getHeapCommittedBytes() {
            return jvmValue(snapshot.jvm().map(TelemetryJvmSnapshot::heapCommittedBytes)
                    .orElse(OptionalLong.empty()));
        }

        @Override
        public long getHeapMaxBytes() {
            return jvmValue(snapshot.jvm().map(TelemetryJvmSnapshot::heapMaxBytes)
                    .orElse(OptionalLong.empty()));
        }

        @Override
        public long getGcCount() {
            return jvmValue(snapshot.jvm().map(TelemetryJvmSnapshot::gcCount)
                    .orElse(OptionalLong.empty()));
        }

        @Override
        public long getGcTimeMillis() {
            return jvmValue(snapshot.jvm().map(TelemetryJvmSnapshot::gcTimeMillis)
                    .orElse(OptionalLong.empty()));
        }

        @Override
        public long getLiveThreadCount() {
            return jvmValue(snapshot.jvm().map(TelemetryJvmSnapshot::liveThreadCount)
                    .orElse(OptionalLong.empty()));
        }

        @Override
        public long getPeakThreadCount() {
            return jvmValue(snapshot.jvm().map(TelemetryJvmSnapshot::peakThreadCount)
                    .orElse(OptionalLong.empty()));
        }

        private static long jvmValue(OptionalLong value) {
            return value.orElse(-1L);
        }
    }
}
