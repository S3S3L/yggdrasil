package org.s3s3l.yggdrasil.utils.es.query.builder;

/**
 * <p>
 * </p>
 * ClassName:BooleanQueryBlockBuilder <br>
 * Date: Jan 2, 2019 1:43:41 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface BooleanQueryBlockBuilder {

    /**
     * 
     * 新增match条件
     * 
     * @param name
     *            字段名
     * @param value
     *            字段值
     * @return {@link BooleanQueryBlockBuilder}
     * @since JDK 1.8
     */
    BooleanQueryBlockBuilder match(String name, String value);

    /**
     * 
     * 新增term条件
     * 
     * @param name
     *            字段名
     * @param value
     *            字段值
     * @return {@link BooleanQueryBlockBuilder}
     * @since JDK 1.8
     */
    BooleanQueryBlockBuilder term(String name, String value);

    /**
     * 
     * 开始否见Rang条件
     * 
     * @return {@link RangeFieldCreator}
     * @since JDK 1.8
     */
    RangeFieldCreator<BooleanQueryBlockBuilder> range();

    /**
     * 
     * bool条件构建完毕
     * 
     * @return {@link AbstractBooleanQueryBuilder}
     * @since JDK 1.8
     */
    AbstractBooleanQueryBuilder boolDone();
}
