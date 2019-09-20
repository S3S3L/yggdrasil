
package org.s3s3l.yggdrasil.spring.env;

import java.lang.annotation.Annotation;

import org.s3s3l.yggdrasil.annotation.apollo.ApolloConfiguration;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;

/**
 * <p>
 * </p>
 * ClassName:ConfigurationBindingPostProcesser <br>
 * Date: Jan 30, 2019 11:05:50 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ConfigurationBindingPostProcesser implements BeanPostProcessor, ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private ConfigurationBeanFactoryMetadata beanFactoryMetadata;

    private Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        ApolloConfiguration acp = getAnnotation(bean, beanName, ApolloConfiguration.class);
        ConfigurationProperties cp = getAnnotation(bean, beanName, ConfigurationProperties.class);
        if (acp == null) {
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        }

        String prefix = StringUtils.EMPTY_STRING;
        if (cp != null) {
            prefix = StringUtils.isEmpty(cp.prefix()) ? cp.value() : cp.prefix();
        }
        return Binder.get(this.environment)
                .bind(prefix, bean.getClass())
                .get();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.beanFactoryMetadata = this.applicationContext.getBean(ConfigurationBeanFactoryMetadata.BEAN_NAME,
                ConfigurationBeanFactoryMetadata.class);
        this.environment = this.applicationContext.getEnvironment();
    }

    private <A extends Annotation> A getAnnotation(Object bean, String beanName, Class<A> type) {
        A annotation = this.beanFactoryMetadata.findFactoryAnnotation(beanName, type);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(bean.getClass(), type);
        }
        return annotation;
    }

}
