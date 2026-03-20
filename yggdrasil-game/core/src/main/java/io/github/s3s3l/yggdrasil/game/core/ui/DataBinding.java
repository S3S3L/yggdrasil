package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * 数据绑定接口
 * 用于实现 UI 组件与数据的双向绑定
 */
public interface DataBinding {

    /**
     * 绑定数据
     *
     * @param data 数据对象
     */
    void bindData(Object data);

    /**
     * 解除绑定
     */
    void unbind();

    /**
     * 刷新绑定数据
     */
    void refresh();
}
