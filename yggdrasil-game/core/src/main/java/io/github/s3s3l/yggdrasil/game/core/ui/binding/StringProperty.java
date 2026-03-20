package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 字符串属性
 */
public class StringProperty extends AbstractProperty<String> {

    public StringProperty() {
        super("");
    }

    public StringProperty(String initialValue) {
        super(initialValue == null ? "" : initialValue);
    }

    public static StringProperty create() {
        return new StringProperty();
    }

    public static StringProperty create(String initialValue) {
        return new StringProperty(initialValue);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value == null ? "" : value);
    }

    public boolean isEmpty() {
        return getValue() == null || getValue().isEmpty();
    }

    public int length() {
        return getValue() == null ? 0 : getValue().length();
    }

    public StringProperty concat(StringProperty other) {
        return new StringProperty(getValue() + other.getValue());
    }

    public StringProperty concat(String other) {
        return new StringProperty(getValue() + other);
    }
}
