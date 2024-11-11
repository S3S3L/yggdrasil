package io.github.s3s3l.yggdrasil.boot.bean.lifecycle;

import io.github.s3s3l.yggdrasil.boot.bean.BeanLifecycle;
import io.github.s3s3l.yggdrasil.boot.exception.BeanCreationException;

public class BuildInstanceByDefaultConstructor implements BeanLifeCycleWorker {
    public static final long PRIORITY = -1_000;

    @SuppressWarnings({ "unchecked" })
    @Override
    public <T> T process(T pre, BeanContext context) {
        if (context.getConstructor() != null && context.getConstructor()
                .getParameters().length == 0) {
            try {
                return (T) context.getConstructor()
                        .newInstance();
            } catch (Exception e) {
                throw new BeanCreationException(e);
            }
        }

        return pre;
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
