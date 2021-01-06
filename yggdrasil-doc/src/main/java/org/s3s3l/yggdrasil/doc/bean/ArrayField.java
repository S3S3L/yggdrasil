package org.s3s3l.yggdrasil.doc.bean;

import org.s3s3l.yggdrasil.doc.enumerations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ArrayField extends BaseField {
    @Getter
    private final FieldType type = FieldType.ARRAY;

    private BaseField elementField;
}
