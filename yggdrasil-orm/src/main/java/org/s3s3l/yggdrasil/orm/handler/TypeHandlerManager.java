package org.s3s3l.yggdrasil.orm.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.s3s3l.yggdrasil.bean.exception.ResourceProcessException;

public class TypeHandlerManager {
    private final Map<Class<? extends TypeHandler>, TypeHandler> handlers = new ConcurrentHashMap<>();

    public TypeHandlerManager addHandler(TypeHandler handler) {
        this.handlers.put(handler.getClass(), handler);
        return this;
    }

    public TypeHandler getHandler(Class<? extends TypeHandler> handlerType) {
        return handlers.get(handlerType);
    }

    public TypeHandler getOrNew(Class<? extends TypeHandler> handlerType) {
        TypeHandler handler = handlers.get(handlerType);

        if (handler == null) {
            try {
                handler = handlerType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ResourceProcessException("handler create fail.", e);
            }
            handlers.put(handlerType, handler);
        }

        return handler;
    }
}
