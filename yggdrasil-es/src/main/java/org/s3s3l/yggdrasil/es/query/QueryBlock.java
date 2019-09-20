package org.s3s3l.yggdrasil.es.query;

/**
 * <p>
 * </p>
 * ClassName:QueryBlock <br>
 * Date: Dec 29, 2018 8:50:32 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface QueryBlock {

    /**
     * 
     * 转换成DSL字符串
     * 
     * @return DSL字符串
     * @since JDK 1.8
     */
    String toQueryString();
}
