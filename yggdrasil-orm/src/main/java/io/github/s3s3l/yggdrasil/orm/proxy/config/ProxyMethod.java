package io.github.s3s3l.yggdrasil.orm.proxy.config;

import io.github.s3s3l.yggdrasil.bean.verify.Examine;
import io.github.s3s3l.yggdrasil.bean.verify.Expectation;

import lombok.Data;

@Data
public class ProxyMethod {
    /**
     * 对应代理接口中的方法名称
     */
    @Examine(value = Expectation.HAS_LENGTH)
    private String method;
    /**
     * 需要执行的sql, 支持freemarker语法和以下语法
     * 
     * @see //TODO: 语法文档
     */
    @Examine(value = Expectation.HAS_LENGTH)
    private String sql;
    /**
     * sql的类型, 默认为QUERY类型
     */
    @Examine(value = Expectation.NOT_NULL)
    private SqlType type = SqlType.QUERY;
}
