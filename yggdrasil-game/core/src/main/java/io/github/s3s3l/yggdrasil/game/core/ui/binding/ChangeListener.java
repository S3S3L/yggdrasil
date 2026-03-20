package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 变化监听器
 * 当值发生变化时调用
 *
 * @param <T> 值的类型
 */
@FunctionalInterface
public interface ChangeListener<T> {

    /**
     * 当值变化时调用
     *
     * @param observable 可观察的对象
     * @param oldValue   旧值
     * @param newValue   新值
     */
    void changed(ObservableValue<? extends T> observable, T oldValue, T newValue);
}
