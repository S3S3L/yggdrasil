package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 属性基类
 *
 * @param <T> 值的类型
 */
public abstract class AbstractProperty<T> implements Property<T> {

    private final List<InvalidationListener> invalidationListeners = new CopyOnWriteArrayList<>();
    private final List<ChangeListener<? super T>> changeListeners = new CopyOnWriteArrayList<>();
    private ObservableValue<? extends T> observable;
    private InvalidationListener bindingListener;
    private T value;
    private boolean locked = false;

    protected AbstractProperty() {
        this(null);
    }

    protected AbstractProperty(T initialValue) {
        this.value = initialValue;
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
    public T getValue() {
        if (observable != null) {
            return observable.getValue();
        }
        return value;
    }

    @Override
    public void setValue(T value) {
        if (isBound()) {
            return;
        }
        setImpl(value);
    }

    protected void setImpl(T value) {
        if (locked) {
            return;
        }
        locked = true;

        T oldValue = this.value;
        this.value = value;

        if (oldValue != value && (oldValue == null || !oldValue.equals(value))) {
            locked = false;
            fireValueChanged(oldValue, value);
        } else {
            locked = false;
        }
    }

    protected void fireValueChanged(T oldValue, T newValue) {
        for (InvalidationListener listener : invalidationListeners) {
            try {
                listener.invalidated(this);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }

        for (ChangeListener<? super T> listener : changeListeners) {
            try {
                ((ChangeListener<T>) listener).changed(this, oldValue, newValue);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    protected void fireValueChanged() {
        fireValueChanged(value, value);
    }

    @Override
    public void bind(ObservableValue<? extends T> observable) {
        if (observable == null) {
            throw new IllegalArgumentException("Cannot bind to null");
        }
        if (observable == this) {
            throw new IllegalArgumentException("Cannot bind to self");
        }
        if (this.observable != observable) {
            unbind();
            this.observable = observable;
            T oldValue = this.value;
            this.value = observable.getValue();
            // 创建并存储绑定监听器
            bindingListener = (ov) -> {
                if (!locked) {
                    T oldVal = value;
                    value = observable.getValue();
                    if (oldVal != value && (oldVal == null || !oldVal.equals(value))) {
                        fireValueChanged(oldVal, value);
                    }
                }
            };
            this.observable.addListener(bindingListener);
            // 触发初始值变化事件
            if (oldValue != this.value && (oldValue == null || !oldValue.equals(this.value))) {
                fireValueChanged(oldValue, this.value);
            }
        }
    }

    @Override
    public void unbind() {
        if (observable != null && bindingListener != null) {
            observable.removeListener(bindingListener);
            bindingListener = null;
            observable = null;
        }
    }

    @Override
    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bindBidirectional(Property<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot bind to null");
        }
        if (other == this) {
            throw new IllegalArgumentException("Cannot bind to self");
        }

        // 同步初始值 - 使用 this 的值同步到 other
        if (getValue() != null) {
            other.setValue(getValue());
        } else if (other.getValue() != null) {
            setValue(other.getValue());
        }

        // 添加双向监听 - 使用原始类型避免类型擦除问题
        addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                if (!locked) {
                    other.setValue(newValue);
                }
            }
        });
        other.addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                if (!locked) {
                    setValue(newValue);
                }
            }
        });
    }

    @Override
    public void unbindBidirectional(Property<T> other) {
        unbind();
        other.unbind();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Object getBean() {
        return null;
    }

    @Override
    public String toString() {
        T val = getValue();
        if (val == null) {
            return "Property[null]";
        }
        return "Property[" + val.toString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof AbstractProperty) {
            AbstractProperty<?> other = (AbstractProperty<?>) obj;
            T thisValue = getValue();
            Object otherValue = other.getValue();
            if (thisValue == null) {
                return otherValue == null;
            }
            if (otherValue == null) {
                return false;
            }
            return thisValue.equals(otherValue);
        }
        return false;
    }

    @Override
    public int hashCode() {
        T val = getValue();
        return val == null ? 0 : val.hashCode();
    }
}
