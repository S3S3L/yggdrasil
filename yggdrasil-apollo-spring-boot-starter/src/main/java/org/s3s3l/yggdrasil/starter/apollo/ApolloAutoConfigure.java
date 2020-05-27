package org.s3s3l.yggdrasil.starter.apollo;

import org.s3s3l.yggdrasil.starter.apollo.feature.PropertySourceHelper;
import org.s3s3l.yggdrasil.starter.apollo.feature.StringToObjectConverter;
import org.s3s3l.yggdrasil.starter.apollo.feature.TreeNodeConverter;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>
 * </p>
 * ClassName:RedisAutoConfigure <br>
 * Date: Sep 10, 2018 9:00:44 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@ConditionalOnProperty(prefix = ApolloConfiguration.PREFIX, name = "enable", havingValue = "true")
public class ApolloAutoConfigure implements EnvironmentPostProcessor, Ordered {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ApolloConfiguration configuration;
    private ConfigurableEnvironment environment;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        PropertySourceHelper psHelper = new PropertySourceHelper(environment);
        this.environment = environment;

        resolveConfiguration();
        if (!this.configuration.isEnable()) {
            return;
        }

        this.environment.getConversionService()
                .addConverter(new TreeNodeConverter());
        this.environment.getConversionService()
                .addConverter(new StringToObjectConverter());
        configuration.getDocs()
                .stream()
                .sorted()
                .forEach(psHelper::processDocument);
    }

    private void resolveConfiguration() {
        this.configuration = Binder.get(environment)
                .bind(ApolloConfiguration.PREFIX, ApolloConfiguration.class)
                .orElse(new ApolloConfiguration());
        String confStr = JacksonUtils.defaultHelper.toStructuralString(this.configuration);
        logger.info("Apollo configuration loaded. {}", confStr);
    }

    @Override
    public int getOrder() {
        return ConfigFileApplicationListener.DEFAULT_ORDER + 1;
    }
}
