package io.github.s3s3l.yggdrasil.boot.bean.creation;

import io.github.s3s3l.yggdrasil.boot.exception.BeanCreationException;

public class BuildInstanceByConstructor implements BeanCreationStep {
    public static final int PRIORITY = BuildInstanceByMethod.PRIORITY + 1_000;

    @Override
    public <T> T process(T pre, CreationContext<T> context) {
        if (context.getConstructor() == null || context.getParams() == null) {
            return pre;
        }

        try {
            return context.getConstructor()
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

}
