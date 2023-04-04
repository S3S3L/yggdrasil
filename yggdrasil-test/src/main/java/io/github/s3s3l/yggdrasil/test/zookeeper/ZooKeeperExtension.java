package io.github.s3s3l.yggdrasil.test.zookeeper;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import io.github.s3s3l.yggdrasil.test.base.ServiceExtension;

public class ZooKeeperExtension implements BeforeEachCallback, AfterEachCallback, AfterAllCallback, ServiceExtension {
    private static final int PORT = 2181;
    // private static final String TAR_BALL_NAME = "zookeeper.tar.gz";
    private static final String IMAGE_NAME = "yggdrasil/zookeeper:v1.0.0";
    private final GenericContainer<?> zookeeper;

    @SuppressWarnings("resource")
    public ZooKeeperExtension(ZooKeeperExtensionConfig config) {
        // ImageFromDockerfile dockerfile = new ImageFromDockerfile();

        // if (StringUtils.isNotEmpty(config.getTarLocation())) {
        //     dockerfile.withFileFromClasspath("Dockerfile", "zk/Dockerfile")
        //             .withFileFromFile(TAR_BALL_NAME,
        //                     new File(FileUtils.getFirstExistResourcePath(config.getTarLocation())));
        // } else {
        //     dockerfile.withFileFromClasspath("Dockerfile", "zk/Dockerfile_remote_tar");
        // }
        zookeeper = new GenericContainer<>(IMAGE_NAME).withExposedPorts(PORT)
                .waitingFor(Wait.forLogMessage(config.getStartedRegex(), 1));
    }

    @Override
    public String getEndPoint() {
        return String.format("%s:%d", zookeeper.getHost(), zookeeper.getMappedPort(PORT));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        zookeeper.stop();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        zookeeper.start();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (zookeeper != null) {
            zookeeper.close();
        }
    }
}
