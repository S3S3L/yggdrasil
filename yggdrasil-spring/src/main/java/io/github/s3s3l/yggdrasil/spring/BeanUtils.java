
package io.github.s3s3l.yggdrasil.spring;

import java.util.Arrays;
import java.util.Map;

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

    /**
     * 构建BeanDefinition
     * 
     * @param beanType
     *            类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Class<?> beanType) {
        return buildBeanDefinition(null, null, null, null, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param props
     *            属性值(fieldName -> object)
     * @param beanType
     *            类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Map<String, Object> props, Class<?> beanType) {
        return buildBeanDefinition(props, null, null, null, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param props
     *            属性值(fieldName -> object)
     * @param refs
     *            属性值(fieldName -> beanName)
     * @param beanType
     *            类型
     * @return
     */
    public static BeanDefinition
            buildBeanDefinition(Map<String, Object> props, Map<String, String> refs, Class<?> beanType) {
        return buildBeanDefinition(props, refs, null, null, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param props
     *            属性值(fieldName -> object)
     * @param refs
     *            属性值(fieldName -> beanName)
     * @param constructArgsRef
     *            构造方法参数(beanName)
     * @param beanType
     *            类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Map<String, Object> props,
            Map<String, String> refs,
            String[] constructArgsRef,
            Class<?> beanType) {
        return buildBeanDefinition(props, refs, constructArgsRef, null, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param constructArgs
     *            构造方法参数(object)
     * @param beanType
     *            类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Object[] constructArgs, Class<?> beanType) {
        return buildBeanDefinition(null, null, null, constructArgs, beanType);
    }

    /**
     * 构建BeanDefinition
     * 
     * @param props
     *            属性值(fieldName -> object)
     * @param refs
     *            属性值(fieldName -> beanName)
     * @param constructArgsRef
     *            构造方法参数(beanName)
     * @param constructArgs
     *            构造方法参数(object)
     * @param beanType
     *            类型
     * @return
     */
    public static BeanDefinition buildBeanDefinition(Map<String, Object> props,
            Map<String, String> refs,
            String[] constructArgsRef,
            Object[] constructArgs,
            Class<?> beanType) {
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

    /**
     * 构建BeanDefinition
     * 
     * @param beanType
     *            类型
     * @param factoryBeanName
     *            工厂beanName
     * @param factoryMethod
     *            工厂方法名
     * @param args
     *            工厂方法入参(object)
     * @param refs
     *            工厂方法入参(beanName)
     * @return
     */
    public static BeanDefinition buildBeanDefinitionForFactoryMethod(Class<?> beanType,
            String factoryBeanName,
            String factoryMethod,
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
