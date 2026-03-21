package io.github.s3s3l.yggdrasil.game.core.render.dirty;

/**
 * 可脏标记对象接口
 * 需要脏标记优化的对象应实现此接口
 */
public interface DirtyMarkable {

    /**
     * 获取对象唯一标识
     * 用于在脏标记集合中区分不同对象
     *
     * @return 对象唯一标识
     */
    long getId();

    /**
     * 当对象被标记为脏时调用
     */
    default void onMarkDirty() {
    }

    /**
     * 当对象的脏标记被清除时调用
     */
    default void onClearDirty() {
    }

    /**
     * 检查对象是否有效
     * 无效的对象不应参与渲染
     *
     * @return true 如果对象有效
     */
    default boolean isValid() {
        return true;
    }
}
