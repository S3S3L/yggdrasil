package org.s3s3l.yggdrasil.utils.es.query.builder;

/**
 * <p>
 * </p>
 * ClassName:BooleanQuerySelector <br>
 * Date: Jan 2, 2019 1:39:59 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface BooleanQuerySelector {
    /**
     * 
     * 开始should条件构建
     * 
     * @return {@link BooleanQueryBlockBuilder}
     * @since JDK 1.8
     */
    BooleanQueryBlockBuilder should();

    /**
     * 
     * 开始must条件构建
     * 
     * @return {@link BooleanQueryBlockBuilder}
     * @since JDK 1.8
     */
    BooleanQueryBlockBuilder must();

    /**
     * 
     * 开始filter条件构建
     * 
     * @return {@link BooleanQueryBlockBuilder}
     * @since JDK 1.8
     */
    BooleanQueryBlockBuilder filter();
}
