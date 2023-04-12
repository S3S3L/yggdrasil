package io.github.s3s3l.yggdrasil.utils.promise;

import java.util.function.Consumer;
import java.util.function.Supplier;

import io.github.s3s3l.yggdrasil.bean.exception.ResourceProcessException;

public class InterceptorBuilder<C> {

    private boolean finished = false;
    private final Class<C> contextType;
    private Supplier<C> buildContext;
    private Consumer<C> beforeAction;

    public static <C> InterceptorBuilder<C> newBuilder(Class<C> contextType) {
        return new InterceptorBuilder<C>(contextType);
    }

    private InterceptorBuilder(Class<C> contextType) {
        this.contextType = contextType;
    }

    public InterceptorBuilder<C> buildContext(Supplier<C> supplier) {
        this.buildContext = supplier;
        return this;
    }

    public InterceptorBuilder<C> beforeAction(Consumer<C> consumer) {
        this.beforeAction = consumer;
        return this;
    }

    public Interceptor build() {
        if (finished) {
            throw new ResourceProcessException("The build process has already finished.");
        }
        finished = true;
        return new Interceptor() {

            @Override
            public Object buildContext() {
                return buildContext == null ? null : buildContext.get();
            }

            @Override
            public void beforeAction(Object context) {
                if (beforeAction == null) {
                    return;
                }
                beforeAction.accept(contextType.cast(context));
            }

            @Override
            public Class<C> getType() {
                return contextType;
            }

        };
    }
}
