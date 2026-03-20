package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import io.github.s3s3l.yggdrasil.game.core.ui.UICanvas;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * UI 场景基类
 * 所有 UI 场景的基础类
 */
public abstract class UIScene {

    private String name;
    private UICanvas canvas;
    private boolean active;
    private boolean paused;

    /**
     * 创建场景
     */
    protected UIScene() {
        this.active = false;
        this.paused = false;
    }

    /**
     * 创建场景
     *
     * @param name 场景名称
     */
    protected UIScene(String name) {
        this();
        this.name = name;
    }

    /**
     * 获取场景名称
     *
     * @return 场景名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置场景名称
     *
     * @param name 场景名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取画布
     *
     * @return 画布
     */
    public UICanvas getCanvas() {
        return canvas;
    }

    /**
     * 设置画布
     *
     * @param canvas 画布
     */
    public void setCanvas(UICanvas canvas) {
        this.canvas = canvas;
    }

    /**
     * 检查场景是否激活
     *
     * @return true 如果场景激活
     */
    public boolean isActive() {
        return active;
    }

    /**
     * 设置场景激活状态
     *
     * @param active 激活状态
     */
    protected void setActive(boolean active) {
        this.active = active;
    }

    /**
     * 检查场景是否暂停
     *
     * @return true 如果场景暂停
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * 设置场景暂停状态
     *
     * @param paused 暂停状态
     */
    protected void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * 场景进入时调用
     * 在此初始化场景资源
     */
    protected void onEnter() {
        active = true;
        paused = false;
    }

    /**
     * 场景离开时调用
     * 在此清理场景资源
     */
    protected void onExit() {
        active = false;
        paused = false;
    }

    /**
     * 场景暂停时调用
     * 场景被其他场景覆盖时触发
     */
    protected void onPause() {
        paused = true;
    }

    /**
     * 场景恢复时调用
     * 覆盖的场景被弹出后触发
     */
    protected void onResume() {
        paused = false;
    }

    /**
     * 更新场景
     *
     * @param deltaTime 时间增量
     */
    public void update(float deltaTime) {
        if (!active || paused) {
            return;
        }
        if (canvas != null) {
            canvas.update(deltaTime);
        }
    }

    /**
     * 渲染场景
     */
    public void render() {
        if (!active) {
            return;
        }
        if (canvas != null) {
            canvas.render();
        }
    }

    /**
     * 添加组件到场景
     *
     * @param component 组件
     */
    public void addComponent(UIComponent component) {
        if (canvas != null) {
            canvas.addChild(component);
        }
    }

    /**
     * 从场景移除组件
     *
     * @param component 组件
     */
    public void removeComponent(UIComponent component) {
        if (canvas != null) {
            canvas.removeChild(component);
        }
    }

    /**
     * 根据名称查找组件
     *
     * @param name 组件名称
     * @return 找到的组件
     */
    public UIComponent findComponentByName(String name) {
        if (canvas != null) {
            return canvas.findChildByName(name);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIScene uiScene = (UIScene) o;
        if (name == null) return false;
        return name.equals(uiScene.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
