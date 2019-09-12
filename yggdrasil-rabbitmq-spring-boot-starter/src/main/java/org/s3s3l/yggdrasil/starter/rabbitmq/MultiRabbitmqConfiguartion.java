package org.s3s3l.yggdrasil.starter.rabbitmq;

import java.util.Map;

import org.s3s3l.yggdrasil.annotation.apollo.ApolloConfiguration;
import org.s3s3l.yggdrasil.configuration.rabbitmq.RabbitmqConnectionConfiguration;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.rabbitmq.client.ConnectionFactory;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:MultiRabbitmqConfiguartion <br>
 * Date: Nov 10, 2018 4:37:53 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
@ConditionalOnMissingBean(RabbitAdmin.class)
@ConditionalOnClass({ ConnectionFactory.class, RabbitAdmin.class })
@ApolloConfiguration
@ConfigurationProperties(prefix = MultiRabbitmqConfiguartion.PREFIX)
public class MultiRabbitmqConfiguartion {
    public static final String PREFIX = "yggdrasil.rabbitmq";

    private boolean enable;
    private Map<String, RabbitmqConfiguartion> mqs;

    @Data
    public static class RabbitmqConfiguartion {
        private RabbitmqConnectionConfiguration conn;
        private Map<String, QueueConfiguration> queues;
        private Map<String, ExchangeConfiguration> exchanges;
        private BindConfiguration[] binds;
    }

    @Data
    public static class QueueConfiguration {
        private String name;
        private boolean durable;
        private boolean exclusive;
        private boolean autoDelete;
        private Class<? extends Queue> type;
    }

    @Data
    public static class ExchangeConfiguration {
        private String name;
        private boolean durable;
        private boolean autoDelete;
        private Map<String, Object> arguments;
        private Class<? extends AbstractExchange> type;
    }

    @Data
    public static class BindConfiguration {
        private String queue;
        private String exchange;
        private String routingKey;
    }
}
