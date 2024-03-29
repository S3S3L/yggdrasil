package io.github.s3s3l.yggdrasil.orm.meta;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConditionContext {
    private Class<?> tableDefineType;
    @Builder.Default
    private List<ConditionMeta> selectConditions = new LinkedList<>();
    @Builder.Default
    private List<ConditionMeta> updateConditions = new LinkedList<>();
    @Builder.Default
    private List<ConditionMeta> deleteConditions = new LinkedList<>();
    private GroupByMeta groupBy;
    @Builder.Default
    private List<OrderByMeta> orderBy = new LinkedList<>();
    private OffsetMeta offset;
    private LimitMeta limit;
}
