package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 可写的值接口
 * 用于设置值
 *
 * @param <T> 值的类型
 */
public interface WritableValue<T> {

    /**
     * 设置值
     */
    void setValue(T value);
}
