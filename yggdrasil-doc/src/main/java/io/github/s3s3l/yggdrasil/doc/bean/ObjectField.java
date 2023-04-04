package io.github.s3s3l.yggdrasil.doc.bean;

import java.util.SortedSet;

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
public class ObjectField extends BaseField {
    /**
     * 类型
     */
    @Getter
    private final FieldType type = FieldType.OBJECT;

    private SortedSet<BaseField> subParams;
}
