package org.s3s3l.yggdrasil.starter.apollo.feature;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>
 * </p>
 * ClassName: ConfigFileChangedProcessor <br>
 * Date: Jun 25, 2019 1:55:33 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@FunctionalInterface
public interface ConfigFileChangedProcessor {

    /**
     * 
     * Processor for doing something after apollo config file is changed.
     * 
     * @param context
     *            spring context
     * @param environment
     *            spring environment
     * @param newValue
     *            apollo config file new value
     * @since JDK 1.8
     */
    void process(ApplicationContext context, ConfigurableEnvironment environment, String newValue);
}
