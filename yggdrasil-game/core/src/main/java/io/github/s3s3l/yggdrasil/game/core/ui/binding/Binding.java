package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 绑定接口
 * 表示一个从其他可观察值计算出的值
 *
 * @param <T> 值的类型
 */
public interface Binding<T> extends ObservableValue<T> {

    /**
     * 获取依赖的可观察值
     */
    ObservableValue<?>[] getDependencies();

    /**
     * 重新计算值
     */
    void recalculate();

    /**
     * 检查是否是有效的（缓存的值是否有效）
     */
    boolean isValid();
}
