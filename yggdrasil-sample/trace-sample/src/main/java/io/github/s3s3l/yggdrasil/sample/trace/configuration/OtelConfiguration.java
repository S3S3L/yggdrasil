package io.github.s3s3l.yggdrasil.sample.trace.configuration;

import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.s3s3l.yggdrasil.otel.pool.OtelDelagateExecutorService;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.instrumentation.log4j.appender.v2_17.OpenTelemetryAppender;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ServiceAttributes;

@Configuration
public class OtelConfiguration {

    @Bean
    OpenTelemetry openTelemetry() {
        Resource resource = Resource.getDefault()
                .toBuilder()
                .put(ServiceAttributes.SERVICE_NAME, "dice-server")
                .put(ServiceAttributes.SERVICE_VERSION, "0.1.0")
                .build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder()
                        .setEndpoint("http://192.168.3.22/:4317")
                        .build())
                        .build())
                .setResource(resource)
                .build();
        // SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
        // .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
        // .setResource(resource)
        // .build();

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
                .registerMetricReader(PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder()
                        .setEndpoint("http://192.168.3.22:4317")
                        .build())
                        .build())
                .setResource(resource)
                .build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(BatchLogRecordProcessor.builder(OtlpGrpcLogRecordExporter.builder()
                        .setEndpoint("http://192.168.3.22:4317")
                        .build())
                        .build())
                .setResource(resource)
                .build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setMeterProvider(sdkMeterProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();

        OpenTelemetryAppender.install(openTelemetry);

        return openTelemetry;
    }

    @Bean
    OtelDelagateExecutorService otelDelagateExecutorService(OpenTelemetry openTelemetry) {
        return new OtelDelagateExecutorService(Executors.newFixedThreadPool(10), openTelemetry);
    }
}
