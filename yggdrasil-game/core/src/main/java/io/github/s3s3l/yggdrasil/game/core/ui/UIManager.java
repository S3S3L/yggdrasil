package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 管理器
 * 管理游戏中的 UI 系统
 */
public class UIManager {

    private static UIManager instance;

    private final List<UICanvas> canvases;
    private UICanvas activeCanvas;
    private boolean enabled;

    private UIManager() {
        this.canvases = new ArrayList<>();
        this.enabled = true;
    }

    /**
     * 获取单例实例
     *
     * @return UI 管理器实例
     */
    public static synchronized UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }

    public List<UICanvas> getCanvases() {
        return canvases;
    }

    public UICanvas getActiveCanvas() {
        return activeCanvas;
    }

    public void setActiveCanvas(UICanvas activeCanvas) {
        this.activeCanvas = activeCanvas;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 添加画布
     *
     * @param canvas 画布
     */
    public void addCanvas(UICanvas canvas) {
        if (!canvases.contains(canvas)) {
            canvases.add(canvas);
            if (activeCanvas == null) {
                activeCanvas = canvas;
            }
        }
    }

    /**
     * 移除画布
     *
     * @param canvas 画布
     */
    public void removeCanvas(UICanvas canvas) {
        canvases.remove(canvas);
        if (activeCanvas == canvas) {
            activeCanvas = canvases.isEmpty() ? null : canvases.get(0);
        }
    }

    /**
     * 获取画布
     *
     * @param name 画布名称
     * @return 画布，如果不存在则返回 null
     */
    public UICanvas getCanvas(String name) {
        for (UICanvas canvas : canvases) {
            if (name.equals(canvas.getName())) {
                return canvas;
            }
        }
        return null;
    }

    /**
     * 更新 UI
     *
     * @param deltaTime 时间增量
     */
    public void update(float deltaTime) {
        if (!enabled) {
            return;
        }

        for (UICanvas canvas : canvases) {
            canvas.update(deltaTime);
        }
    }

    /**
     * 渲染 UI
     */
    public void render() {
        if (!enabled) {
            return;
        }

        for (UICanvas canvas : canvases) {
            canvas.render();
        }
    }

    /**
     * 处理点击
     *
     * @param x 点击 X 坐标
     * @param y 点击 Y 坐标
     * @return 是否处理了点击
     */
    public boolean handleClick(float x, float y) {
        if (!enabled) {
            return false;
        }

        // 从后向前遍历（后面的画布优先）
        for (int i = canvases.size() - 1; i >= 0; i--) {
            UICanvas canvas = canvases.get(i);
            if (canvas.isEnabled() && canvas.isVisible()) {
                if (canvas.handleClick(x, y)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 处理悬停
     *
     * @param x 鼠标 X 坐标
     * @param y 鼠标 Y 坐标
     */
    public void handleHover(float x, float y) {
        if (!enabled) {
            return;
        }

        for (UICanvas canvas : canvases) {
            if (canvas.isEnabled() && canvas.isVisible()) {
                canvas.handleHover(x, y);
            }
        }
    }

    /**
     * 处理文本输入
     *
     * @param text 输入的文本
     * @return 是否处理了输入
     */
    public boolean handleTextInput(String text) {
        if (!enabled || activeCanvas == null) {
            return false;
        }

        return activeCanvas.handleTextInput(text);
    }

    /**
     * 处理按键
     *
     * @param keyCode 按键代码
     * @return 是否处理了按键
     */
    public boolean handleKeyPress(int keyCode) {
        if (!enabled || activeCanvas == null) {
            return false;
        }

        return activeCanvas.handleKeyPress(keyCode);
    }

    /**
     * 显示 UI
     *
     * @param name UI 名称
     */
    public void showUI(String name) {
        UICanvas canvas = getCanvas(name);
        if (canvas != null) {
            canvas.setEnabled(true);
            canvas.setVisibility(UIVisibility.VISIBLE);
        }
    }

    /**
     * 隐藏 UI
     *
     * @param name UI 名称
     */
    public void hideUI(String name) {
        UICanvas canvas = getCanvas(name);
        if (canvas != null) {
            canvas.setEnabled(false);
            canvas.setVisibility(UIVisibility.HIDDEN);
        }
    }

    /**
     * 切换 UI 可见性
     *
     * @param name UI 名称
     */
    public void toggleUI(String name) {
        UICanvas canvas = getCanvas(name);
        if (canvas != null) {
            if (canvas.isVisible()) {
                hideUI(name);
            } else {
                showUI(name);
            }
        }
    }
}
