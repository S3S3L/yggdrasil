package io.github.s3s3l.yggdrasil.otel.data.es;


import io.github.s3s3l.yggdrasil.otel.data.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class DataPiece extends BasicData {
    public abstract DataType getType();
    
}
