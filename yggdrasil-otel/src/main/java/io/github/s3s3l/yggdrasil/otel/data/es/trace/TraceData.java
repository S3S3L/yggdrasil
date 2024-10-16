package io.github.s3s3l.yggdrasil.otel.data.es.trace;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.s3s3l.yggdrasil.otel.data.es.DataPiece;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TraceData extends DataPiece {
    @JsonProperty("Duration")
    private long duration;
    @JsonProperty("EndTimestamp")
    private ZonedDateTime endTimestamp;
    /**
     * @see SpanKind
     */
    @JsonProperty("Kind")
    private String kind;
    @JsonProperty("Link")
    private String link;
    @JsonProperty("Name")
    private String name;
    /**
     * @see StatusCode
     */
    @JsonProperty("TraceStatus")
    private int traceStatus;
}
