package org.s3s3l.yggdrasil.starter.rabbitmq;

import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * <p>
 * </p>
 * ClassName: RabbitmqAutoConfigure <br>
 * date: Nov 10, 2018 4:28:08 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@EnableConfigurationProperties(MultiRabbitmqConfiguartion.class)
@ConditionalOnClass({ ConnectionFactory.class, RabbitAdmin.class })
@ConditionalOnProperty(prefix = MultiRabbitmqConfiguartion.PREFIX, name = "enable", havingValue = "true")
public class RabbitmqAutoConfigureAfterApp
        implements ApplicationListener<ApplicationReadyEvent>, ApplicationEventPublisherAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationEventPublisher publisher;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.trace("Starting auto-configuring soav2.");
        ConfigurableApplicationContext ctx = event.getApplicationContext();

        MultiRabbitmqConfiguartion configuration = ctx.getBean(MultiRabbitmqConfiguartion.class);
        String configurationStr = JacksonUtils.defaultHelper.toStructuralString(configuration);
        logger.debug("soav2 configuration '{}'", configurationStr);

        logger.trace("Finished auto-configuring soav2.");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

}
