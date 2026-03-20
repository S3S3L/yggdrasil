package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 无效监听器
 * 当被观察的值失效时调用
 */
@FunctionalInterface
public interface InvalidationListener {

    /**
     * 当值失效时调用
     *
     * @param observable 可观察的对象
     */
    void invalidated(ObservableValue<?> observable);
}
