package io.github.s3s3l.yggdrasil.doc.bean;

import io.github.s3s3l.yggdrasil.doc.enumerations.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArrayField extends BaseField {
    @Getter
    private final FieldType type = FieldType.ARRAY;

    private BaseField elementField;
}
