package io.github.s3s3l.yggdrasil.test.nacos;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import io.github.s3s3l.yggdrasil.test.base.ServiceExtension;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NacosExtension implements BeforeEachCallback, AfterEachCallback, AfterAllCallback, ServiceExtension {
    private static final String IMAGE_NAME = "nacos/nacos-server:v2.2.0";
    @SuppressWarnings("deprecation")
    private FixedHostPortGenericContainer<?> nacos = new FixedHostPortGenericContainer<>(IMAGE_NAME).withEnv("MODE",
            "standalone");
    private final NacosExtensionConfig config;

    public NacosExtension(NacosExtensionConfig config) {
        this.config = config;
        int port = config.getPort();
        int grpcSdkPort = port + 1000;
        int grpcClusterPort = port + 1001;
        int raftPort = port - 1000;
        nacos.withFixedExposedPort(port, port)
                .withFixedExposedPort(grpcSdkPort, grpcSdkPort)
                .withFixedExposedPort(grpcClusterPort, grpcClusterPort)
                .withFixedExposedPort(raftPort, raftPort)
                .waitingFor(Wait.forLogMessage(config.getStartedRegex(), 1));
    }

    public String getEndPoint() {
        return String.format("%s:%d", nacos.getHost(), config.getPort());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        nacos.stop();
        log.debug("nacos-server stoped.");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        nacos.start();
        log.debug("nacos-server started on port: {}", config.getPort());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        nacos.close();
    }

}
