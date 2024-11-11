package io.github.s3s3l.yggdrasil.sample.trace.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "es")
public class EsClientConfig {
    private String endpoint;
    private String apiKey;

    private String traceIndex;
    private String logIndex;
    private String otelIndexes;
}
