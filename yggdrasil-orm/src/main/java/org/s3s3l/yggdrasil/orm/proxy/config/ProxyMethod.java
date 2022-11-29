package org.s3s3l.yggdrasil.orm.proxy.config;

import org.s3s3l.yggdrasil.bean.verify.Examine;
import org.s3s3l.yggdrasil.bean.verify.Expectation;

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
}
