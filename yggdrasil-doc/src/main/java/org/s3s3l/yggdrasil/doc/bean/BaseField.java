package org.s3s3l.yggdrasil.doc.bean;

import org.s3s3l.yggdrasil.doc.enumerations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseField implements Order, Comparable<BaseField> {
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private FieldType type;
    /**
     * 是否必须
     */
    @Builder.Default
    private boolean required = false;
    /**
     * 描述
     */
    private String comment;

    /**
     * 排序位
     */
    @Builder.Default
    private int order = 0;

    @Override
    public int compareTo(BaseField o) {
        return this.order - o.order;
    }
}
