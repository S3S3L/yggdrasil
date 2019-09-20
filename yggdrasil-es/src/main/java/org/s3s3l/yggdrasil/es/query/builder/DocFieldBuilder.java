package org.s3s3l.yggdrasil.es.query.builder;

/**
 * <p>
 * </p>
 * ClassName:DocFieldBuilder <br>
 * Date: Jan 7, 2019 11:31:44 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface DocFieldBuilder {

    /**
     * 
     * 添加文档字段
     * 
     * @param name
     *            字段名
     * @param value
     *            字段值
     * @return {@link DocFieldBuilder}
     * @since JDK 1.8
     */
    DocFieldBuilder field(String name, String value);

    /**
     * 
     * 完成文档构建
     * 
     * @return {@link EsdslBuilder}
     * @since JDK 1.8
     */
    EsdslBuilder done();
}
