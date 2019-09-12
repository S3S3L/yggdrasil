package org.s3s3l.yggdrasil.utils.es.query.builder;

import org.s3s3l.yggdrasil.utils.es.query.Esdsl;

/**
 * <p>
 * </p>
 * ClassName:DocBeanBuilder <br>
 * Date: Jan 7, 2019 11:33:47 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface DocBeanBuilder {

    /**
     * 
     * 通过一个对象构建文档的DSL
     * 
     * @param bean
     *            数据对象
     * @return {@link Esdsl}
     * @since JDK 1.8
     */
    Esdsl bean(Object bean);
}
