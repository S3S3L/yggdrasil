package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 可观察的值接口
 * 用于观察值的变化
 *
 * @param <T> 值的类型
 */
public interface ObservableValue<T> {

    /**
     * 获取值
     */
    T getValue();

    /**
     * 添加无效监听器
     */
    void addListener(InvalidationListener listener);

    /**
     * 移除无效监听器
     */
    void removeListener(InvalidationListener listener);

    /**
     * 添加变化监听器
     */
    void addListener(ChangeListener<? super T> listener);

    /**
     * 移除变化监听器
     */
    void removeListener(ChangeListener<? super T> listener);
}
