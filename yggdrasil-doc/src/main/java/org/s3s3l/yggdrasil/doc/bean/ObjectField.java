package org.s3s3l.yggdrasil.doc.bean;

import java.util.SortedSet;

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
public class ObjectField extends BaseField {
    /**
     * 类型
     */
    @Getter
    private final FieldType type = FieldType.OBJECT;

    private SortedSet<BaseField> subParams;
}
