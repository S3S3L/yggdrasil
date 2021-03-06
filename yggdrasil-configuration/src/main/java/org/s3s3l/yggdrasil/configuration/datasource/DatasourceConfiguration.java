package org.s3s3l.yggdrasil.configuration.datasource;

import lombok.Data;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
}
