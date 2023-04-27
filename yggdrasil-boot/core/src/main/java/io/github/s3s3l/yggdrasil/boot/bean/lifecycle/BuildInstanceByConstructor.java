package io.github.s3s3l.yggdrasil.boot.bean.lifecycle;

import io.github.s3s3l.yggdrasil.boot.bean.BeanLifecycle;
import io.github.s3s3l.yggdrasil.boot.exception.BeanCreationException;

public class BuildInstanceByConstructor implements BeanLifeCycleWorker {
    public static final int PRIORITY = BuildInstanceByMethod.PRIORITY + 1;

    @SuppressWarnings({ "unchecked" })
    @Override
    public <T> T process(T pre, BeanContext context) {
        if (context.getConstructor() == null || context.getParams() == null) {
            return pre;
        }

        try {
            return (T) context.getConstructor()
                    .newInstance(context.getParams()
                            .get());
        } catch (Exception e) {
            throw new BeanCreationException(e);
        }
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public BeanLifecycle getLifecycle() {
        return BeanLifecycle.CREATING;
    }

}
