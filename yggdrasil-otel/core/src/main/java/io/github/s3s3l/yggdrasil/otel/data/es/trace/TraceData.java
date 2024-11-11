package io.github.s3s3l.yggdrasil.otel.data.es.trace;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.s3s3l.yggdrasil.otel.data.DataType;
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
    @JsonAlias("Duration")
    private long duration;
    @JsonAlias("EndTimestamp")
    private ZonedDateTime endTimestamp;
    /**
     * @see SpanKind
     */
    @JsonAlias("Kind")
    private String kind;
    @JsonAlias("Link")
    private String link;
    @JsonAlias("Name")
    private String name;
    /**
     * @see StatusCode
     */
    @JsonProperty("TraceStatus")
    private int traceStatus;

    @Override
    public DataType getType() {
        return DataType.TRACE;
    }
}
