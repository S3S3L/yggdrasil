package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定注解
 * 用于标记需要进行数据绑定的字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {

    /**
     * 绑定的源属性路径
     * 格式："objectName.propertyName"
     */
    String value();

    /**
     * 是否双向绑定
     */
    boolean bidirectional() default false;

    /**
     * 转换器类
     */
    Class<? extends Converter<?, ?>> converter() default Converter.None.class;
}
