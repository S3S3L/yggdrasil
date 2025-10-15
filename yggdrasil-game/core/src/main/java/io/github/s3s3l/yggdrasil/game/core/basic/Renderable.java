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
     * 执行当前对象的计算逻辑，在渲染前调用，此过程可并发执行。
     * 主要用于更新对象的状态，如位置、动画等。
     * 所有对象均执行。
     * 
     * @param delta 距离上次渲染间隔时间，单位秒
     */
    void calculate(float delta);

    /**
     * 执行当前对象的预渲染逻辑，在计算后调用，此过程可并发执行。
     * 主要用于更新渲染所需的资源，如顶点缓冲区、纹理等。
     * 仅对脏对象执行。
     */
    void preRender();

    /**
     * 执行当前对象的渲染，在预渲染后调用，此过程不可并发执行
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