package io.github.s3s3l.yggdrasil.otel.data.es;

import java.time.ZonedDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class DataPiece {
    @JsonProperty("@timestamp")
    private ZonedDateTime timestamp;
    @JsonProperty("SpanId")
    private String spanId;
    @JsonProperty("TraceId")
    private String traceId;
    @JsonProperty("Attributes")
    private Map<String, Object> attributes;
    @JsonProperty("Resource")
    private Resource resource;
    @JsonProperty("Scope")
    private Scope scope;
    
}
