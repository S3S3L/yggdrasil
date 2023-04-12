package io.github.s3s3l.yggdrasil.boot.bean.creation;

import io.github.s3s3l.yggdrasil.boot.exception.BeanCreationException;

public class BuildInstanceByDefaultConstructor implements BeanCreationStep {
    public static final int PRIORITY = Integer.MIN_VALUE;

    @Override
    public <T> T process(T pre, CreationContext<T> context) {
        if (context.getConstructor() != null && context.getConstructor()
                .getParameters().length == 0) {
            try {
                return context.getConstructor()
                        .newInstance();
            } catch (Exception e) {
                throw new BeanCreationException(e);
            }
        }

        return pre;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

}
