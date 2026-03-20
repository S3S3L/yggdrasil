package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 属性接口
 * 结合可观察和可写的值
 *
 * @param <T> 值的类型
 */
public interface Property<T> extends ObservableValue<T>, WritableValue<T> {

    /**
     * 绑定到另一个可观察的值
     */
    void bind(ObservableValue<? extends T> observable);

    /**
     * 解除绑定
     */
    void unbind();

    /**
     * 检查是否已绑定
     */
    boolean isBound();

    /**
     * 双向绑定
     */
    void bindBidirectional(Property<T> other);

    /**
     * 解除双向绑定
     */
    void unbindBidirectional(Property<T> other);

    /**
     * 获取属性名称
     */
    String getName();

    /**
     * 获取对象（该属性所属的对象）
     */
    Object getBean();
}
