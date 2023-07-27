package io.github.s3s3l.yggdrasil.orm.meta;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableMeta {

    /**
     * 表名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 字段
     */
    @Builder.Default
    private List<ColumnMeta> columns = new LinkedList<>();
}
