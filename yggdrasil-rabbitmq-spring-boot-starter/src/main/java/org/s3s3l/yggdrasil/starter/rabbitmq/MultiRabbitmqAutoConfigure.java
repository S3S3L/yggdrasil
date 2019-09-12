
package org.s3s3l.yggdrasil.starter.rabbitmq;

import java.util.Map.Entry;

import org.s3s3l.yggdrasil.starter.rabbitmq.MultiRabbitmqConfiguartion.RabbitmqConfiguartion;
import org.s3s3l.yggdrasil.utils.json.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import com.rabbitmq.client.ConnectionFactory;

/**
 * <p>
 * </p>
 * ClassName:RabbitmqAutoConfigure <br>
 * Date: Nov 10, 2018 5:24:54 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@EnableConfigurationProperties(MultiRabbitmqConfiguartion.class)
@ConditionalOnClass({ ConnectionFactory.class, RabbitAdmin.class })
@ConditionalOnMissingBean(RabbitAdmin.class)
@ConditionalOnProperty(prefix = MultiRabbitmqConfiguartion.PREFIX, name = "enable", havingValue = "true")
public class MultiRabbitmqAutoConfigure implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final String BEAN_NAME = "multiRabbitmqAutoConfigure";
    private static final String RabbitAdmin_TAIL = "RabbitAdmin";
    private static final String QUEUE_TAIL = "Queue";
    private static final String EXCHANGE_TAIL = "Exchange";
    private static final String BIND_TAIL = "Bind";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    }
    
    public void multiRabbitmqAutoConfigure(MultiRabbitmqConfiguartion configuration, BeanDefinitionRegistry registry) {
        logger.trace("Starting auto-configuring multiple datasource.");
        String configurationStr = JacksonUtils.defaultHelper.toJsonString(configuration);
        logger.debug("Multiple datasource configuration. '{}'", configurationStr);

        logger.trace("Starting registering common datasources.");
        for (Entry<String, RabbitmqConfiguartion> entry : configuration.getMqs()
                .entrySet()) {
            String key = entry.getKey();
            RabbitmqConfiguartion value = entry.getValue();

        }
        logger.trace("Finished registering common datasources.");
        
    }

    private MultiRabbitmqConfiguartion resolveConfiguration() {
        return new Binder(ConfigurationPropertySources.get(environment))
                .bind(MultiRabbitmqConfiguartion.PREFIX, MultiRabbitmqConfiguartion.class)
                .orElse(new MultiRabbitmqConfiguartion());
    }

}
