package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * Float 属性
 */
public class FloatProperty extends AbstractProperty<Float> {

    public FloatProperty() {
        super(0.0f);
    }

    public FloatProperty(float initialValue) {
        super(initialValue);
    }

    public static FloatProperty create() {
        return new FloatProperty();
    }

    public static FloatProperty create(float initialValue) {
        return new FloatProperty(initialValue);
    }

    public float get() {
        Float value = getValue();
        return value == null ? 0.0f : value;
    }

    public void set(float value) {
        setValue(value);
    }

    public FloatProperty add(float value) {
        return new FloatProperty(get() + value);
    }

    public FloatProperty subtract(float value) {
        return new FloatProperty(get() - value);
    }

    public FloatProperty multiply(float value) {
        return new FloatProperty(get() * value);
    }

    public FloatProperty divide(float value) {
        return new FloatProperty(get() / value);
    }
}
