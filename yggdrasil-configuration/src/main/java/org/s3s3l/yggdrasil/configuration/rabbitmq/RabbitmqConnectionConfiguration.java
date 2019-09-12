package org.s3s3l.yggdrasil.configuration.rabbitmq;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.ConnectionFactory;

/**
 * <p>
 * </p>
 * ClassName:RabbitmqConnectionConfiguration <br>
 * Date: Nov 21, 2016 2:03:03 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@ConditionalOnClass(ConnectionFactory.class)
@Configuration
@ConfigurationProperties(prefix = "rabbitmq.conn")
public class RabbitmqConnectionConfiguration extends ConnectionFactory {

}
