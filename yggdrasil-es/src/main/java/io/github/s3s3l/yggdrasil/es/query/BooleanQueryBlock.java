package io.github.s3s3l.yggdrasil.es.query;

import java.util.List;

/**
 * <p>
 * </p>
 * ClassName:BooleanQueryBlock <br>
 * Date: Jan 2, 2019 11:26:58 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface BooleanQueryBlock extends RangableQueryBlock {

    /**
     * 
     * 设置条件
     * 
     * @param querys 条件
     * @return {@link BooleanQueryBlock}
     * @since JDK 1.8
     */
    BooleanQueryBlock conditions(QueryBlock... querys);

    /**
     * 
     * 设置条件
     * 
     * @param querys 条件
     * @return {@link BooleanQueryBlock}
     * @since JDK 1.8
     */
    BooleanQueryBlock conditions(List<QueryBlock> querys);

    /**
     * 
     * 新增区间查询条件
     * 
     * @param range 区间查询条件
     * @return {@link BooleanQueryBlock}
     * @since JDK 1.8
     */
    BooleanQueryBlock range(Range range);

    /**
     * 
     * 新增match查询条件
     * 
     * @param name 字段名称
     * @param value 字段值
     * @return {@link BooleanQueryBlock}
     * @since JDK 1.8
     */
    BooleanQueryBlock match(String name, String value);

    /**
     * 
     * 新增term查询条件
     * 
     * @param name 字段名称
     * @param value 字段值
     * @return {@link BooleanQueryBlock}
     * @since JDK 1.8
     */
    BooleanQueryBlock term(String name, String value);
}
