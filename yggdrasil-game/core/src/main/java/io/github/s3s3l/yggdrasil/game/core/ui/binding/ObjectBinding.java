package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * 对象绑定实现
 *
 * @param <T> 值的类型
 */
public abstract class ObjectBinding<T> implements Binding<T> {

    private final List<InvalidationListener> invalidationListeners = new CopyOnWriteArrayList<>();
    private final List<ChangeListener<? super T>> changeListeners = new CopyOnWriteArrayList<>();
    private final ObservableValue<?>[] dependencies;
    private T value;
    private boolean valid = false;
    private boolean locked = false;

    protected ObjectBinding(ObservableValue<?>... dependencies) {
        this.dependencies = dependencies;
        for (ObservableValue<?> dep : dependencies) {
            if (dep != null) {
                dep.addListener((ov) -> {
                    if (!locked) {
                        invalidate();
                    }
                });
            }
        }
    }

    protected abstract T computeValue();

    @Override
    public T getValue() {
        if (!valid) {
            value = computeValue();
            valid = true;
        }
        return value;
    }

    protected void invalidate() {
        if (valid) {
            valid = false;
            locked = true;
            try {
                for (InvalidationListener listener : invalidationListeners) {
                    try {
                        listener.invalidated(this);
                    } catch (Exception e) {
                        // 忽略
                    }
                }

                T oldValue = value;
                T newValue = getValue();
                if (oldValue != newValue && (oldValue == null || !oldValue.equals(newValue))) {
                    for (ChangeListener<? super T> listener : changeListeners) {
                        try {
                            ((ChangeListener<T>) listener).changed(this, oldValue, newValue);
                        } catch (Exception e) {
                            // 忽略
                        }
                    }
                }
            } finally {
                locked = false;
            }
        }
    }

    @Override
    public void addListener(InvalidationListener listener) {
        if (listener != null && !invalidationListeners.contains(listener)) {
            invalidationListeners.add(listener);
        }
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }

    @Override
    public void addListener(ChangeListener<? super T> listener) {
        if (listener != null && !changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        changeListeners.remove(listener);
    }

    @Override
    public ObservableValue<?>[] getDependencies() {
        return Arrays.copyOf(dependencies, dependencies.length);
    }

    @Override
    public void recalculate() {
        invalidate();
        getValue();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    /**
     * 获取值（便捷方法）
     */
    public T get() {
        return getValue();
    }
}
