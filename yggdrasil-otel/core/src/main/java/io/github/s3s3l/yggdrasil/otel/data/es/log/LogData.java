package io.github.s3s3l.yggdrasil.otel.data.es.log;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.github.s3s3l.yggdrasil.otel.data.DataType;
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
    @JsonAlias("Body")
    private String body;
    @JsonAlias("SeverityNumber")
    private int severityNumber;
    @JsonAlias("SeverityText")
    private String serverityText;
    @JsonAlias("TraceFlags")
    private int traceFlags;

    @Override
    public DataType getType() {
        return DataType.LOG;
    }
}
