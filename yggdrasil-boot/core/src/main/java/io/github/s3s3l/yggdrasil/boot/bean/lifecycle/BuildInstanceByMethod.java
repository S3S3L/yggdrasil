package io.github.s3s3l.yggdrasil.boot.bean.lifecycle;

import java.util.Optional;

import io.github.s3s3l.yggdrasil.boot.bean.BeanLifecycle;
import io.github.s3s3l.yggdrasil.boot.exception.BeanCreationException;

public class BuildInstanceByMethod implements BeanLifeCycleWorker {
    public static final long PRIORITY = BuildInstanceByDefaultConstructor.PRIORITY + 1;

    @SuppressWarnings({ "unchecked" })
    @Override
    public <T> T process(T pre, BeanContext context) {
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
    public long getPriority() {
        return PRIORITY;
    }

    @Override
    public BeanLifecycle getLifecycle() {
        return BeanLifecycle.CREATING;
    }

}
