package io.github.s3s3l.yggdrasil.doc.node;

import java.util.SortedSet;

import io.github.s3s3l.yggdrasil.doc.bean.BaseField;
import io.github.s3s3l.yggdrasil.doc.enumerations.Protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDocNode extends DocNode {

    /**
     * 接口的协议，默认是{@code NATIVE}
     * 
     * @protocol
     */
    @Builder.Default
    private Protocol protocol = Protocol.NATIVE;
    /**
     * 接口地址，默认是{@code ${className}#${methodName}}
     * 
     * @path
     */
    private String path;
    private SortedSet<BaseField> params;
    private SortedSet<BaseField> response;
}
