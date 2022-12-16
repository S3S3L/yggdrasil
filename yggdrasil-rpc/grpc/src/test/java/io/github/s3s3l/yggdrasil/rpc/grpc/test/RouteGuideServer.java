/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.s3s3l.yggdrasil.rpc.grpc.test;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.github.s3s3l.yggdrasil.rpc.grpc.test.routeguide.Feature;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.service.RouteGuideService;
import io.github.s3s3l.yggdrasil.rpc.grpc.test.utils.RouteGuideUtil;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * A sample gRPC server that serve the RouteGuide (see route_guide.proto)
 * service.
 */
public class RouteGuideServer {
    private static final Logger logger = Logger.getLogger(RouteGuideServer.class.getName());

    public static final int PORT = 3742;

    private final int port;
    private final Server server;

    public RouteGuideServer(int port) throws IOException {
        this(port, RouteGuideUtil.getDefaultFeaturesFile());
    }

    /**
     * Create a RouteGuide server listening on {@code port} using
     * {@code featureFile} database.
     */
    public RouteGuideServer(int port, URL featureFile) throws IOException {
        this(Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create()), port,
                RouteGuideUtil.parseFeatures(featureFile));
    }

    /**
     * Create a RouteGuide server using serverBuilder as a base and features as
     * data.
     */
    public RouteGuideServer(ServerBuilder<?> serverBuilder, int port, Collection<Feature> features) {
        this.port = port;
        server = serverBuilder.addService(new RouteGuideService(features))
                .build();
    }

    /** Start serving requests. */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime()
                .addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        // Use stderr here since the logger may have been reset
                        // by its JVM shutdown hook.
                        System.err.println("*** shutting down gRPC server since JVM is shutting down");
                        try {
                            RouteGuideServer.this.stop();
                        } catch (InterruptedException e) {
                            e.printStackTrace(System.err);
                        }
                        System.err.println("*** server shut down");
                    }
                });
    }

    /** Stop serving requests and shutdown resources. */
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown()
                    .awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon
     * threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main method. This comment makes the linter happy.
     */
    public static void main(String[] args) throws Exception {
        RouteGuideServer server = new RouteGuideServer(PORT);
        server.start();
        server.blockUntilShutdown();
    }
}
