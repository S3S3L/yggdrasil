package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 整数属性
 */
public class IntegerProperty extends AbstractProperty<Integer> {

    public IntegerProperty() {
        super(0);
    }

    public IntegerProperty(int initialValue) {
        super(initialValue);
    }

    public static IntegerProperty create() {
        return new IntegerProperty();
    }

    public static IntegerProperty create(int initialValue) {
        return new IntegerProperty(initialValue);
    }

    public int get() {
        Integer value = getValue();
        return value == null ? 0 : value;
    }

    public void set(int value) {
        setValue(value);
    }

    public IntegerBinding add(int value) {
        return Bindings.addIntegers(this, new IntegerProperty(value));
    }

    public IntegerBinding subtract(int value) {
        return Bindings.subtractIntegers(this, new IntegerProperty(value));
    }

    public IntegerBinding multiply(int value) {
        return Bindings.multiplyIntegers(this, new IntegerProperty(value));
    }

    public IntegerBinding divide(int value) {
        return Bindings.divideIntegers(this, new IntegerProperty(value));
    }

    public IntegerBinding add(IntegerProperty other) {
        return Bindings.addIntegers(this, other);
    }

    public IntegerBinding subtract(IntegerProperty other) {
        return Bindings.subtractIntegers(this, other);
    }

    public IntegerBinding multiply(IntegerProperty other) {
        return Bindings.multiplyIntegers(this, other);
    }

    public IntegerBinding divide(IntegerProperty other) {
        return Bindings.divideIntegers(this, other);
    }
}
