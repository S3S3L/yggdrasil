package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * Double 属性
 */
public class DoubleProperty extends AbstractProperty<Double> {

    public DoubleProperty() {
        super(0.0);
    }

    public DoubleProperty(double initialValue) {
        super(initialValue);
    }

    public static DoubleProperty create() {
        return new DoubleProperty();
    }

    public static DoubleProperty create(double initialValue) {
        return new DoubleProperty(initialValue);
    }

    public double get() {
        Double value = getValue();
        return value == null ? 0.0 : value;
    }

    public void set(double value) {
        setValue(value);
    }

    public DoubleBinding add(double value) {
        return Bindings.addDoubles(this, new DoubleProperty(value));
    }

    public DoubleBinding subtract(double value) {
        return Bindings.subtractDoubles(this, new DoubleProperty(value));
    }

    public DoubleBinding multiply(double value) {
        return Bindings.multiplyDoubles(this, new DoubleProperty(value));
    }

    public DoubleBinding divide(double value) {
        return Bindings.divideDoubles(this, new DoubleProperty(value));
    }

    public DoubleBinding add(DoubleProperty other) {
        return Bindings.addDoubles(this, other);
    }

    public DoubleBinding subtract(DoubleProperty other) {
        return Bindings.subtractDoubles(this, other);
    }

    public DoubleBinding multiply(DoubleProperty other) {
        return Bindings.multiplyDoubles(this, other);
    }

    public DoubleBinding divide(DoubleProperty other) {
        return Bindings.divideDoubles(this, other);
    }
}
