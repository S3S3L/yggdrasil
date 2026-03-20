package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 对象属性
 *
 * @param <T> 对象类型
 */
public class ObjectProperty<T> extends AbstractProperty<T> {

    public ObjectProperty() {
        super(null);
    }

    public ObjectProperty(T initialValue) {
        super(initialValue);
    }

    public static <T> ObjectProperty<T> create() {
        return new ObjectProperty<>();
    }

    public static <T> ObjectProperty<T> create(T initialValue) {
        return new ObjectProperty<>(initialValue);
    }

    public T get() {
        return getValue();
    }

    public void set(T value) {
        setValue(value);
    }
}
