package io.github.s3s3l.yggdrasil.starter.datasource.config;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManagerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:DatasourceConfiguration <br>
 * Date: Aug 31, 2016 5:14:23 PM <br>
 *
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
@ConditionalOnClass(PoolProperties.class)
@Configuration("datasourceConfiguration")
@ConfigurationProperties(prefix = "datasource")
public class DatasourceConfiguration extends PoolProperties {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 6502361085513435695L;

    private AutoCreateConfig autoCreate = new AutoCreateConfig();
    private MetaManagerConfig meta;
}
