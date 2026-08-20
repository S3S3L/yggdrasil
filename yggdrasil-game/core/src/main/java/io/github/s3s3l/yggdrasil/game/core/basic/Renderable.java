package io.github.s3s3l.yggdrasil.game.core.basic;

public interface Renderable extends ID {

    /**
     * 窗口大小变化时调用
     * 
     * @param width  新的窗口宽度
     * @param height 新的窗口高度
     */
    void onWindowResize(int width, int height);

    /**
     * 执行当前对象的计算逻辑，在渲染前调用。调度器可显式并行此阶段，但不得执行 GL 操作。
     * 主要用于更新对象的状态，如位置、动画等。
     * 所有对象均执行。
     * 
     * @param delta 距离上次渲染间隔时间，单位秒
     */
    void calculate(float delta);

    /**
     * 执行当前对象的预渲染逻辑，在计算后调用。调度器可显式并行此阶段，但不得执行 GL 操作。
     * 主要用于准备纯 CPU 渲染数据。
     * 仅对脏对象执行。
     */
    void preRender();

    /**
     * 执行当前对象的渲染，在预渲染后调用。GL 操作只能发生在前端拥有的 render thread。
     * 所有对象均执行。
     */
    void render();

    /**
     * 检查当前对象是否为脏对象（即状态是否有变更）
     * 
     * @return
     */
    boolean isDirty();

    /**
     * 标记当前对象为脏对象
     */
    void markDirty();

    /**
     * 清除当前对象的脏对象标记
     */
    void clearDirty();

}