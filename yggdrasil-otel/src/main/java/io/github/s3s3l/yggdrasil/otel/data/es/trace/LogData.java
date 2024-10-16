package io.github.s3s3l.yggdrasil.otel.data.es.trace;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.s3s3l.yggdrasil.otel.data.es.DataPiece;
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
public class LogData extends DataPiece {
    @JsonProperty("Body")
    private String body;
    @JsonProperty("SeverityNumber")
    private int severityNumber;
    @JsonProperty("SeverityText")
    private String serverityText;
    @JsonProperty("TraceFlags")
    private int traceFlags;
}
