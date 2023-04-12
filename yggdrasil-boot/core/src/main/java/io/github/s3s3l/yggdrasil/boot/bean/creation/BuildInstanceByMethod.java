package io.github.s3s3l.yggdrasil.boot.bean.creation;

import java.util.Optional;

import io.github.s3s3l.yggdrasil.boot.exception.BeanCreationException;

public class BuildInstanceByMethod implements BeanCreationStep {
    public static final int PRIORITY = BuildInstanceByDefaultConstructor.PRIORITY + 1_000;

    @SuppressWarnings({ "unchecked" })
    @Override
    public <T> T process(T pre, CreationContext<T> context) {
        if (context.getFactoryMethod() == null || context.getFactoryBean() == null || context.getParams() == null) {
            return pre;
        }
        try {
            Object[] params = Optional.ofNullable(context.getParams()
                    .get())
                    .orElse(new Object[] {});
            return (T) context.getFactoryMethod()
                    .invoke(context.getFactoryBean()
                            .get(), params);
        } catch (Exception e) {
            throw new BeanCreationException(e);
        }
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

}
