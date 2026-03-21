package io.github.s3s3l.yggdrasil.game.core.pool;

/**
 * 可池化对象接口
 * 池化对象需要实现此接口以支持重置和归还回调
 *
 * @param <T> 实现类的类型
 */
public interface PoolableObject<T extends PoolableObject<T>> {

    /**
     * 重置对象状态
     * 当对象从池中借出时调用
     */
    void reset();

    /**
     * 归还到池中时的回调
     * 可以在此方法中清理临时状态
     */
    void onReturn();

    /**
     * 验证对象是否有效
     * 归还对象时会调用此方法验证
     *
     * @return true 如果对象有效可以归还
     */
    default boolean isValid() {
        return true;
    }
}
