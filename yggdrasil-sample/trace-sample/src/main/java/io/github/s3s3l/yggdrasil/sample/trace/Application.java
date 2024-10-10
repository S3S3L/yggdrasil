package io.github.s3s3l.yggdrasil.sample.trace;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
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

@SpringBootApplication(scanBasePackages = { "io.github.s3s3l.yggdrasil.sample.trace" })
public class Application {
    public static void main(String[] args) throws IOException {
        // SpringApplication.run(Application.class, args);
        testEs();
    }

    static void testEs() throws IOException {
        // URL and API key
        String serverUrl = "https://localhost:9200";
        String apiKey = "NHVwQmRKSUJQTktEV0NpLWNKY0Q6MlVNU2hiQ0ZSdEMxTW0wYW9VNS1Fdw==";

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[] {
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // Use the client...

        // Close the transport, freeing the underlying thread
        transport.close();
    }

    @Bean
    public OpenTelemetry openTelemetry() {
        Resource resource = Resource.getDefault().toBuilder()
                .put(ServiceAttributes.SERVICE_NAME, "dice-server")
                .put(ServiceAttributes.SERVICE_VERSION, "0.1.0")
                .build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(
                        BatchSpanProcessor
                                .builder(OtlpGrpcSpanExporter.builder().setEndpoint("http://localhost:4317").build())
                                .build())
                .setResource(resource)
                .build();
        // SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
        // .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
        // .setResource(resource)
        // .build();

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
                .registerMetricReader(
                        PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder()
                                .setEndpoint("http://localhost:4317").build()).build())
                .setResource(resource)
                .build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(
                        BatchLogRecordProcessor.builder(OtlpHttpLogRecordExporter.builder()
                                .setEndpoint("http://localhost:3100/otlp/v1/logs").build())
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
}
