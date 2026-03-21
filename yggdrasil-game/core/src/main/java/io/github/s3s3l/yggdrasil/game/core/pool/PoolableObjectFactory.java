package io.github.s3s3l.yggdrasil.game.core.pool;

/**
 * 池化对象工厂接口
 * 用于创建、销毁和验证池化对象
 *
 * @param <T> 池化对象类型
 */
public interface PoolableObjectFactory<T extends PoolableObject<T>> {

    /**
     * 创建新的池化对象
     *
     * @return 新创建的对象
     * @throws Exception 创建失败时抛出
     */
    T create() throws Exception;

    /**
     * 销毁对象
     * 当对象被丢弃或池被清空时调用
     *
     * @param obj 要销毁的对象
     * @throws Exception 销毁失败时抛出
     */
    void destroy(T obj) throws Exception;

    /**
     * 验证对象是否有效
     *
     * @param obj 要验证的对象
     * @return true 如果对象有效
     * @throws Exception 验证失败时抛出
     */
    boolean validate(T obj) throws Exception;
}
