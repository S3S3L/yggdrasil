package io.github.s3s3l.yggdrasil.otel.data.es;

import java.time.ZonedDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.github.s3s3l.yggdrasil.otel.data.Resource;
import io.github.s3s3l.yggdrasil.otel.data.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasicData {
    @JsonAlias("@timestamp")
    private ZonedDateTime timestamp;
    @JsonAlias("ParentSpanId")
    private String parentSpanId;
    @JsonAlias("SpanId")
    private String spanId;
    @JsonAlias("TraceId")
    private String traceId;
    @JsonAlias("Attributes")
    private Map<String, Object> attributes;
    @JsonAlias("Resource")
    private Resource resource;
    @JsonAlias("Scope")
    private Scope scope;
    
}
