package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 约束布局
 * 通过约束关系定位子组件
 */
public class UILayoutConstraintLayout extends UILayoutBase {

    private Map<UIComponent, ConstraintSet> constraintSets;

    public UILayoutConstraintLayout() {
        this.constraintSets = new HashMap<>();
    }

    /**
     * 设置组件的约束
     */
    public void setConstraints(UIComponent child, ConstraintSet constraints) {
        if (child != null && constraints != null) {
            constraintSets.put(child, constraints);
            markDirty();
        }
    }

    /**
     * 获取组件的约束
     */
    public ConstraintSet getConstraints(UIComponent child) {
        return constraintSets.get(child);
    }

    /**
     * 移除组件的约束
     */
    public void removeConstraints(UIComponent child) {
        constraintSets.remove(child);
        markDirty();
    }

    /**
     * 清除所有约束
     */
    public void clearConstraints() {
        constraintSets.clear();
        markDirty();
    }

    @Override
    public void addChild(UIComponent child) {
        super.addChild(child);
        // 为新添加的子组件创建默认约束
        if (!constraintSets.containsKey(child)) {
            constraintSets.put(child, new ConstraintSet());
        }
    }

    @Override
    public void removeChild(UIComponent child) {
        super.removeChild(child);
        constraintSets.remove(child);
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        constraintSets.clear();
    }

    @Override
    protected void onLayout(float width, float height) {
        if (children.isEmpty()) {
            return;
        }

        float contentWidth = width - paddingLeft - paddingRight;
        float contentHeight = height - paddingTop - paddingBottom;

        // 第一遍：计算所有组件的初始位置和大小
        for (UIComponent child : children) {
            ConstraintSet constraints = constraintSets.get(child);
            if (constraints == null) {
                constraints = new ConstraintSet();
            }

            LayoutParams lp = getChildLayoutParams(child);

            float x = calculateX(child, constraints, lp, contentWidth, width);
            float y = calculateY(child, constraints, lp, contentHeight, height);
            float childWidth = calculateWidth(child, constraints, lp, contentWidth);
            float childHeight = calculateHeight(child, constraints, lp, contentHeight);

            child.setX(x + paddingLeft);
            child.setY(y + paddingTop);
            if (childWidth > 0) child.setWidth(childWidth);
            if (childHeight > 0) child.setHeight(childHeight);
        }

        // 第二遍：处理相对约束（依赖其他组件的位置）
        boolean changed = true;
        int iterations = 0;
        int maxIterations = children.size() + 1;

        while (changed && iterations < maxIterations) {
            changed = false;
            iterations++;

            for (UIComponent child : children) {
                ConstraintSet constraints = constraintSets.get(child);
                if (constraints == null) continue;

                LayoutParams lp = getChildLayoutParams(child);

                float newX = child.getX();
                float newY = child.getY();

                // 处理相对于其他组件的约束
                if (constraints.startToStartOf != null) {
                    UIComponent target = constraints.startToStartOf;
                    newX = target.getX() + constraints.startMargin;
                } else if (constraints.startToEndOf != null) {
                    UIComponent target = constraints.startToEndOf;
                    newX = target.getX() + target.getWidth() + constraints.startMargin;
                } else if (constraints.endToStartOf != null) {
                    UIComponent target = constraints.endToStartOf;
                    newX = target.getX() - child.getWidth() - constraints.endMargin;
                } else if (constraints.endToEndOf != null) {
                    UIComponent target = constraints.endToEndOf;
                    newX = target.getX() + target.getWidth() - child.getWidth() - constraints.endMargin;
                }

                if (constraints.topToTopOf != null) {
                    UIComponent target = constraints.topToTopOf;
                    newY = target.getY() + constraints.topMargin;
                } else if (constraints.topToBottomOf != null) {
                    UIComponent target = constraints.topToBottomOf;
                    newY = target.getY() + target.getHeight() + constraints.topMargin;
                } else if (constraints.bottomToTopOf != null) {
                    UIComponent target = constraints.bottomToTopOf;
                    newY = target.getY() - child.getHeight() - constraints.bottomMargin;
                } else if (constraints.bottomToBottomOf != null) {
                    UIComponent target = constraints.bottomToBottomOf;
                    newY = target.getY() + target.getHeight() - child.getHeight() - constraints.bottomMargin;
                }

                if (newX != child.getX() || newY != child.getY()) {
                    child.setX(newX);
                    child.setY(newY);
                    changed = true;
                }
            }
        }
    }

    private float calculateX(UIComponent child, ConstraintSet constraints, LayoutParams lp, float contentWidth, float parentWidth) {
        float x = 0;

        // 处理水平居中
        if (constraints.centerHorizontally) {
            float childWidth = getChildWidth(child, lp, contentWidth);
            x = (contentWidth - childWidth) / 2;
            return x;
        }

        // 处理约束
        if (constraints.startToEndOf != null) {
            x = constraints.startMargin;
        } else if (constraints.endToStartOf != null) {
            x = contentWidth - getChildWidth(child, lp, contentWidth) - constraints.endMargin;
        } else {
            // 默认从左边开始
            x = constraints.startMargin;
        }

        return x;
    }

    private float calculateY(UIComponent child, ConstraintSet constraints, LayoutParams lp, float contentHeight, float parentHeight) {
        float y = 0;

        // 处理垂直居中
        if (constraints.centerVertically) {
            float childHeight = getChildHeight(child, lp, contentHeight);
            y = (contentHeight - childHeight) / 2;
            return y;
        }

        // 处理约束
        if (constraints.topToBottomOf != null) {
            y = constraints.topMargin;
        } else if (constraints.bottomToTopOf != null) {
            y = contentHeight - getChildHeight(child, lp, contentHeight) - constraints.bottomMargin;
        } else {
            // 默认从顶部开始
            y = constraints.topMargin;
        }

        return y;
    }

    private float getChildWidth(UIComponent child, LayoutParams lp, float availableWidth) {
        if (lp.getWidth() == LayoutParams.MATCH_PARENT) {
            return availableWidth - lp.getMarginLeft() - lp.getMarginRight();
        } else if (lp.getWidth() == LayoutParams.WRAP_CONTENT) {
            return child.getWidth();
        } else if (lp.getWidth() > 0) {
            return lp.getWidth();
        }
        return child.getWidth();
    }

    private float getChildHeight(UIComponent child, LayoutParams lp, float availableHeight) {
        if (lp.getHeight() == LayoutParams.MATCH_PARENT) {
            return availableHeight - lp.getMarginTop() - lp.getMarginBottom();
        } else if (lp.getHeight() == LayoutParams.WRAP_CONTENT) {
            return child.getHeight();
        } else if (lp.getHeight() > 0) {
            return lp.getHeight();
        }
        return child.getHeight();
    }

    private float calculateWidth(UIComponent child, ConstraintSet constraints, LayoutParams lp, float availableWidth) {
        // 处理宽度约束
        if (constraints.widthPercent > 0) {
            return availableWidth * constraints.widthPercent - lp.getMarginLeft() - lp.getMarginRight();
        }

        return getChildWidth(child, lp, availableWidth);
    }

    private float calculateHeight(UIComponent child, ConstraintSet constraints, LayoutParams lp, float availableHeight) {
        // 处理高度约束
        if (constraints.heightPercent > 0) {
            return availableHeight * constraints.heightPercent - lp.getMarginTop() - lp.getMarginBottom();
        }

        return getChildHeight(child, lp, availableHeight);
    }

    @Override
    protected float[] onMeasure() {
        float totalWidth = paddingLeft + paddingRight;
        float totalHeight = paddingTop + paddingBottom;

        for (UIComponent child : children) {
            ConstraintSet constraints = constraintSets.get(child);
            LayoutParams lp = getChildLayoutParams(child);

            float childWidth = lp.getWidth() == LayoutParams.WRAP_CONTENT ?
                child.getWidth() : (lp.getWidth() > 0 ? lp.getWidth() : 0);
            float childHeight = lp.getHeight() == LayoutParams.WRAP_CONTENT ?
                child.getHeight() : (lp.getHeight() > 0 ? lp.getHeight() : 0);

            if (constraints != null) {
                if (constraints.widthPercent > 0) {
                    childWidth = totalWidth * constraints.widthPercent;
                }
                if (constraints.heightPercent > 0) {
                    childHeight = totalHeight * constraints.heightPercent;
                }
            }

            totalWidth = Math.max(totalWidth, childWidth + lp.getMarginLeft() + lp.getMarginRight());
            totalHeight = Math.max(totalHeight, childHeight + lp.getMarginTop() + lp.getMarginBottom());
        }

        return new float[]{totalWidth, totalHeight};
    }

    /**
     * 约束集合
     */
    public static class ConstraintSet {
        // 水平约束
        public UIComponent startToStartOf;
        public UIComponent startToEndOf;
        public UIComponent endToStartOf;
        public UIComponent endToEndOf;
        public float startMargin = 0;
        public float endMargin = 0;

        // 垂直约束
        public UIComponent topToTopOf;
        public UIComponent topToBottomOf;
        public UIComponent bottomToTopOf;
        public UIComponent bottomToBottomOf;
        public float topMargin = 0;
        public float bottomMargin = 0;

        // 居中对齐
        public boolean centerHorizontally = false;
        public boolean centerVertically = false;

        // 百分比尺寸
        public float widthPercent = 0;
        public float heightPercent = 0;

        public ConstraintSet() {
        }

        /**
         * 设置左边约束
         */
        public ConstraintSet connectStartToLeft(float margin) {
            this.startToStartOf = null;
            this.startToEndOf = null;
            this.startMargin = margin;
            return this;
        }

        /**
         * 设置右边约束
         */
        public ConstraintSet connectStartToRight(UIComponent target, float margin) {
            this.startToEndOf = target;
            this.startMargin = margin;
            return this;
        }

        /**
         * 设置上边约束
         */
        public ConstraintSet connectTopToTop(float margin) {
            this.topToTopOf = null;
            this.topToBottomOf = null;
            this.topMargin = margin;
            return this;
        }

        /**
         * 设置下边约束
         */
        public ConstraintSet connectTopToBottom(UIComponent target, float margin) {
            this.topToBottomOf = target;
            this.topMargin = margin;
            return this;
        }

        /**
         * 水平居中
         */
        public ConstraintSet centerHorizontally() {
            this.centerHorizontally = true;
            return this;
        }

        /**
         * 垂直居中
         */
        public ConstraintSet centerVertically() {
            this.centerVertically = true;
            return this;
        }

        /**
         * 设置宽度百分比
         */
        public ConstraintSet setWidthPercent(float percent) {
            this.widthPercent = percent;
            return this;
        }

        /**
         * 设置高度百分比
         */
        public ConstraintSet setHeightPercent(float percent) {
            this.heightPercent = percent;
            return this;
        }
    }
}
