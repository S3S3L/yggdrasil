package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 布尔属性
 */
public class BooleanProperty extends AbstractProperty<Boolean> {

    public BooleanProperty() {
        super(false);
    }

    public BooleanProperty(boolean initialValue) {
        super(initialValue);
    }

    public static BooleanProperty create() {
        return new BooleanProperty();
    }

    public static BooleanProperty create(boolean initialValue) {
        return new BooleanProperty(initialValue);
    }

    public boolean get() {
        Boolean value = getValue();
        return value != null && value;
    }

    public void set(boolean value) {
        setValue(value);
    }

    public BooleanBinding not() {
        return Bindings.not(this);
    }

    public BooleanBinding and(BooleanProperty other) {
        return Bindings.and(this, other);
    }

    public BooleanBinding or(BooleanProperty other) {
        return Bindings.or(this, other);
    }

    public BooleanBinding and(boolean other) {
        return Bindings.and(this, new BooleanProperty(other));
    }

    public BooleanBinding or(boolean other) {
        return Bindings.or(this, new BooleanProperty(other));
    }
}
