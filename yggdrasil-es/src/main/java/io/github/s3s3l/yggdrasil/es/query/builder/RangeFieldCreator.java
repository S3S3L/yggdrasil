package io.github.s3s3l.yggdrasil.es.query.builder;

/**
 * <p>
 * </p>
 * ClassName:RangeFieldCreator <br>
 * Date: Jan 3, 2019 1:21:44 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface RangeFieldCreator<T> {

    /**
     * 
     * 开始区间条件字段构建
     * 
     * @param name
     *            字段名
     * @return {@link RangeFieldBuilder}
     * @since JDK 1.8
     */
    RangeFieldBuilder<T> field(String name);

    /**
     * 
     * 完成区间条件构建
     * 
     * @return
     * @since JDK 1.8
     */
    T rangeDone();
}
