package io.github.s3s3l.yggdrasil.sample.trace.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "otel")
public class OtelConfig {
    private String collectorEndPoint;
}
