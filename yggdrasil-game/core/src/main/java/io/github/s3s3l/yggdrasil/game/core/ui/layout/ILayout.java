package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 布局管理器接口
 * 所有布局管理器的统一接口
 */
public interface ILayout {

    /**
     * 执行布局
     * @param parent 父组件
     * @param width 布局宽度
     * @param height 布局高度
     */
    void layout(UIComponent parent, float width, float height);

    /**
     * 测量子组件所需空间
     * @param parent 父组件
     * @return 所需的尺寸 [width, height]
     */
    float[] measure(UIComponent parent);

    /**
     * 添加子组件
     */
    void addChild(UIComponent child);

    /**
     * 移除子组件
     */
    void removeChild(UIComponent child);

    /**
     * 清除所有子组件
     */
    void clearChildren();

    /**
     * 获取子组件列表
     */
    java.util.List<UIComponent> getChildren();

    /**
     * 获取子组件数量
     */
    int getChildCount();

    /**
     * 获取指定索引的子组件
     */
    UIComponent getChildAt(int index);

    /**
     * 标记布局为脏（需要重新布局）
     */
    void markDirty();

    /**
     * 清除脏标记
     */
    void clearDirty();

    /**
     * 检查是否需要重新布局
     */
    boolean isDirty();
}
