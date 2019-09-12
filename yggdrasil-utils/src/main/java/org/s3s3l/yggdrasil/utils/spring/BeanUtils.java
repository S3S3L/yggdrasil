
package org.s3s3l.yggdrasil.utils.spring;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * <p>
 * </p>
 * ClassName:BeanUtils <br>
 * Date: Sep 20, 2018 11:52:13 AM <br>
 *
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class BeanUtils {

    public static BeanDefinition buildBeanDefinition(@Nonnull Class<?> beanType) {
        return buildBeanDefinition(null, null, null, null, beanType);
    }

    public static BeanDefinition buildBeanDefinition(Map<String, Object> props, @Nonnull Class<?> beanType) {
        return buildBeanDefinition(props, null, null, null, beanType);
    }

    public static BeanDefinition
    buildBeanDefinition(Map<String, Object> props, Map<String, String> refs, @Nonnull Class<?> beanType) {
        return buildBeanDefinition(props, refs, null, null, beanType);
    }

    public static BeanDefinition buildBeanDefinition(Map<String, Object> props,
                                                     Map<String, String> refs,
                                                     String[] constructArgsRef,
                                                     @Nonnull Class<?> beanType) {
        return buildBeanDefinition(props, refs, constructArgsRef, null, beanType);
    }

    public static BeanDefinition buildBeanDefinition(
            Object[] constructArgs,
            @Nonnull Class<?> beanType) {
        return buildBeanDefinition(null, null, null, constructArgs, beanType);
    }

    public static BeanDefinition buildBeanDefinition(Map<String, Object> props,
                                                     Map<String, String> refs,
                                                     String[] constructArgsRef,
                                                     Object[] constructArgs,
                                                     @Nonnull Class<?> beanType) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanType);
        if (props != null) {
            props.entrySet()
                    .forEach(entry -> builder.addPropertyValue(entry.getKey(), entry.getValue()));
        }
        if (refs != null) {
            refs.entrySet()
                    .forEach(entry -> builder.addPropertyReference(entry.getKey(), entry.getValue()));
        }
        if (constructArgsRef != null) {
            Arrays.stream(constructArgsRef)
                    .forEach(builder::addConstructorArgReference);
        }
        if (constructArgs != null) {
            Arrays.stream(constructArgs)
                    .forEach(builder::addConstructorArgValue);
        }

        builder.setLazyInit(true);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        return builder.getBeanDefinition();
    }

    public static BeanDefinition buildBeanDefinitionForFactoryMethod(@Nonnull Class<?> beanType,
                                                                     @Nonnull String factoryBeanName,
                                                                     @Nonnull String factoryMethod,
                                                                     Object[] args,
                                                                     String... refs) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanType);
        builder.setFactoryMethodOnBean(factoryMethod, factoryBeanName);
        if (args != null) {
            for (Object arg : args) {
                builder.addConstructorArgValue(arg);
            }
        }
        for (String ref : refs) {
            builder.addConstructorArgReference(ref);
        }
        builder.setLazyInit(true);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        return builder.getBeanDefinition();
    }
}
