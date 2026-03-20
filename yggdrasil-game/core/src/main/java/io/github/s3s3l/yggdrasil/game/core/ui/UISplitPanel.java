package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 分割面板组件
 * 用于创建可调整大小的分割区域
 */
public class UISplitPanel extends UIComponent {

    private UIComponent firstComponent;           // 第一个组件
    private UIComponent secondComponent;          // 第二个组件
    private SplitOrientation orientation;         // 分割方向
    private float splitPosition;                  // 分割位置（0-1 或像素值）
    private boolean useProportional;              // 是否使用比例模式
    private float minFirstSize;                   // 第一个组件最小尺寸
    private float minSecondSize;                  // 第二个组件最小尺寸
    private float splitterWidth;                  // 分割条宽度
    private UIColor splitterColor;                // 分割条颜色
    private boolean draggable;                    // 是否可拖动
    private SplitPanelListener listener;          // 监听器
    private boolean isDragging;                   // 是否正在拖动

    /**
     * 分割方向枚举
     */
    public enum SplitOrientation {
        HORIZONTAL,     // 水平分割（上下）
        VERTICAL        // 垂直分割（左右）
    }

    /**
     * 分割面板监听器接口
     */
    public interface SplitPanelListener {
        /**
         * 当分割位置改变时调用
         *
         * @param panel 分割面板
         * @param position 新位置
         */
        void onSplitPositionChanged(UISplitPanel panel, float position);

        /**
         * 当拖动开始时调用
         *
         * @param panel 分割面板
         */
        void onDragStarted(UISplitPanel panel);

        /**
         * 当拖动结束时调用
         *
         * @param panel 分割面板
         */
        void onDragEnded(UISplitPanel panel);
    }

    public UISplitPanel() {
        this(SplitOrientation.VERTICAL);
    }

    public UISplitPanel(SplitOrientation orientation) {
        this.orientation = orientation;
        this.splitPosition = 0.5f;
        this.useProportional = true;
        this.minFirstSize = 50;
        this.minSecondSize = 50;
        this.splitterWidth = 4;
        this.splitterColor = new UIColor(0.3f, 0.3f, 0.3f, 1f);
        this.draggable = true;
    }

    public UIComponent getFirstComponent() {
        return firstComponent;
    }

    public void setFirstComponent(UIComponent firstComponent) {
        this.firstComponent = firstComponent;
        if (firstComponent != null) {
            firstComponent.setParent(this);
        }
    }

    public UIComponent getSecondComponent() {
        return secondComponent;
    }

    public void setSecondComponent(UIComponent secondComponent) {
        this.secondComponent = secondComponent;
        if (secondComponent != null) {
            secondComponent.setParent(this);
        }
    }

    public SplitOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(SplitOrientation orientation) {
        this.orientation = orientation;
    }

    public float getSplitPosition() {
        return splitPosition;
    }

    public void setSplitPosition(float splitPosition) {
        float oldPosition = this.splitPosition;
        this.splitPosition = splitPosition;
        if (oldPosition != splitPosition && listener != null) {
            listener.onSplitPositionChanged(this, splitPosition);
        }
    }

    public boolean isUseProportional() {
        return useProportional;
    }

    public void setUseProportional(boolean useProportional) {
        this.useProportional = useProportional;
    }

    public float getMinFirstSize() {
        return minFirstSize;
    }

    public void setMinFirstSize(float minFirstSize) {
        this.minFirstSize = minFirstSize;
    }

    public float getMinSecondSize() {
        return minSecondSize;
    }

    public void setMinSecondSize(float minSecondSize) {
        this.minSecondSize = minSecondSize;
    }

    public float getSplitterWidth() {
        return splitterWidth;
    }

    public void setSplitterWidth(float splitterWidth) {
        this.splitterWidth = splitterWidth;
    }

    public UIColor getSplitterColor() {
        return splitterColor;
    }

    public void setSplitterColor(UIColor splitterColor) {
        this.splitterColor = splitterColor;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public SplitPanelListener getListener() {
        return listener;
    }

    public void setListener(SplitPanelListener listener) {
        this.listener = listener;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    /**
     * 获取分割条的位置
     *
     * @return 分割条位置（像素值）
     */
    public float getSplitterPosition() {
        if (orientation == SplitOrientation.VERTICAL) {
            float totalWidth = getWidth();
            if (useProportional) {
                return splitPosition * totalWidth;
            } else {
                return splitPosition;
            }
        } else {
            float totalHeight = getHeight();
            if (useProportional) {
                return splitPosition * totalHeight;
            } else {
                return splitPosition;
            }
        }
    }

    /**
     * 设置分割条位置（像素值）
     *
     * @param position 位置
     */
    public void setSplitterPosition(float position) {
        if (orientation == SplitOrientation.VERTICAL) {
            float totalWidth = getWidth();
            float clampedPosition = Math.max(minFirstSize, Math.min(totalWidth - minSecondSize - splitterWidth, position));
            if (useProportional) {
                this.splitPosition = clampedPosition / totalWidth;
            } else {
                this.splitPosition = clampedPosition;
            }
        } else {
            float totalHeight = getHeight();
            float clampedPosition = Math.max(minFirstSize, Math.min(totalHeight - minSecondSize - splitterWidth, position));
            if (useProportional) {
                this.splitPosition = clampedPosition / totalHeight;
            } else {
                this.splitPosition = clampedPosition;
            }
        }
        if (listener != null) {
            listener.onSplitPositionChanged(this, splitPosition);
        }
    }

    /**
     * 获取第一个组件的边界
     *
     * @return 边界数组 [x, y, width, height]
     */
    public float[] getFirstComponentBounds() {
        float[] bounds = new float[4];
        if (orientation == SplitOrientation.VERTICAL) {
            float splitterPos = getSplitterPosition();
            bounds[0] = getX();
            bounds[1] = getY();
            bounds[2] = splitterPos - getX();
            bounds[3] = getHeight();
        } else {
            float splitterPos = getSplitterPosition();
            bounds[0] = getX();
            bounds[1] = getY();
            bounds[2] = getWidth();
            bounds[3] = splitterPos - getY();
        }
        return bounds;
    }

    /**
     * 获取第二个组件的边界
     *
     * @return 边界数组 [x, y, width, height]
     */
    public float[] getSecondComponentBounds() {
        float[] bounds = new float[4];
        if (orientation == SplitOrientation.VERTICAL) {
            float splitterPos = getSplitterPosition();
            bounds[0] = splitterPos + splitterWidth;
            bounds[1] = getY();
            bounds[2] = getX() + getWidth() - bounds[0];
            bounds[3] = getHeight();
        } else {
            float splitterPos = getSplitterPosition();
            bounds[0] = getX();
            bounds[1] = splitterPos + splitterWidth;
            bounds[2] = getWidth();
            bounds[3] = getY() + getHeight() - bounds[1];
        }
        return bounds;
    }

    /**
     * 获取分割条的边界
     *
     * @return 边界数组 [x, y, width, height]
     */
    public float[] getSplitterBounds() {
        float[] bounds = new float[4];
        if (orientation == SplitOrientation.VERTICAL) {
            float splitterPos = getSplitterPosition();
            bounds[0] = splitterPos - splitterWidth / 2;
            bounds[1] = getY();
            bounds[2] = splitterWidth;
            bounds[3] = getHeight();
        } else {
            float splitterPos = getSplitterPosition();
            bounds[0] = getX();
            bounds[1] = splitterPos - splitterWidth / 2;
            bounds[2] = getWidth();
            bounds[3] = splitterWidth;
        }
        return bounds;
    }

    /**
     * 检查点击是否在分割条上
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 是否点击在分割条上
     */
    public boolean isClickOnSplitter(float x, float y) {
        float[] bounds = getSplitterBounds();
        return x >= bounds[0] && x <= bounds[0] + bounds[2] &&
               y >= bounds[1] && y <= bounds[1] + bounds[3];
    }

    /**
     * 处理鼠标按下事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleMouseDown(float x, float y) {
        if (draggable && isClickOnSplitter(x, y)) {
            isDragging = true;
            if (listener != null) {
                listener.onDragStarted(this);
            }
        }
    }

    /**
     * 处理鼠标释放事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleMouseUp(float x, float y) {
        if (isDragging) {
            isDragging = false;
            if (listener != null) {
                listener.onDragEnded(this);
            }
        }
    }

    /**
     * 处理鼠标移动事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleMouseMove(float x, float y) {
        if (isDragging) {
            if (orientation == SplitOrientation.VERTICAL) {
                setSplitterPosition(x);
            } else {
                setSplitterPosition(y);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // 更新子组件的位置和大小
        if (firstComponent != null) {
            float[] firstBounds = getFirstComponentBounds();
            firstComponent.setBounds(firstBounds[0], firstBounds[1], firstBounds[2], firstBounds[3]);
        }
        if (secondComponent != null) {
            float[] secondBounds = getSecondComponentBounds();
            secondComponent.setBounds(secondBounds[0], secondBounds[1], secondBounds[2], secondBounds[3]);
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        renderBackground();

        // 渲染第一个组件
        if (firstComponent != null) {
            firstComponent.render();
        }

        // 渲染第二个组件
        if (secondComponent != null) {
            secondComponent.render();
        }

        // 渲染分割条
        renderSplitter();
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染分割条
     */
    protected void renderSplitter() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 交换两个组件的位置
     */
    public void swapComponents() {
        UIComponent temp = firstComponent;
        firstComponent = secondComponent;
        secondComponent = temp;

        // 交换后调整分割位置
        if (useProportional) {
            splitPosition = 1.0f - splitPosition;
        }
    }

    /**
     * 重置分割位置到中间
     */
    public void resetSplitPosition() {
        setSplitPosition(0.5f);
    }

    /**
     * 获取推荐的宽度
     *
     * @return 推荐宽度
     */
    public float getPreferredWidth() {
        float firstWidth = firstComponent != null && firstComponent instanceof UIPanel ? firstComponent.getWidth() : 200;
        float secondWidth = secondComponent != null && secondComponent instanceof UIPanel ? secondComponent.getWidth() : 200;

        if (orientation == SplitOrientation.VERTICAL) {
            return firstWidth + secondWidth + splitterWidth;
        } else {
            return Math.max(firstWidth, secondWidth);
        }
    }

    /**
     * 获取推荐的高度
     *
     * @return 推荐高度
     */
    public float getPreferredHeight() {
        float firstHeight = firstComponent != null && firstComponent instanceof UIPanel ? firstComponent.getHeight() : 200;
        float secondHeight = secondComponent != null && secondComponent instanceof UIPanel ? secondComponent.getHeight() : 200;

        if (orientation == SplitOrientation.VERTICAL) {
            return Math.max(firstHeight, secondHeight);
        } else {
            return firstHeight + secondHeight + splitterWidth;
        }
    }
}
