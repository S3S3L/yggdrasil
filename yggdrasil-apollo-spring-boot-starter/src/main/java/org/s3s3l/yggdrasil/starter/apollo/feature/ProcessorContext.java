package org.s3s3l.yggdrasil.starter.apollo.feature;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 * </p>
 * ClassName:ProcessorContext <br>
 * Date: Jul 15, 2019 8:53:13 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ProcessorContext implements ApplicationContextAware {
    protected static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

}
