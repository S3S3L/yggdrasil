package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 布局系统
 * 负责 UI 组件的布局和排列
 */
public class UILayout {

    private final List<UILayoutRule> rules;

    public UILayout() {
        this.rules = new ArrayList<>();
    }

    /**
     * 添加布局规则
     *
     * @param rule 布局规则
     */
    public void addRule(UILayoutRule rule) {
        rules.add(rule);
    }

    /**
     * 移除布局规则
     *
     * @param rule 布局规则
     */
    public void removeRule(UILayoutRule rule) {
        rules.remove(rule);
    }

    /**
     * 清除所有布局规则
     */
    public void clearRules() {
        rules.clear();
    }

    /**
     * 应用布局
     *
     * @param root 根组件
     */
    public void apply(UIComponent root) {
        if (root == null) {
            return;
        }

        // 应用所有规则
        for (UILayoutRule rule : rules) {
            rule.apply(root);
        }

        // 递归应用到子组件
        for (UIComponent child : root.getChildren()) {
            apply(child);
        }
    }

    /**
     * 布局规则接口
     */
    public interface UILayoutRule {
        /**
         * 应用规则到组件
         *
         * @param component 目标组件
         */
        void apply(UIComponent component);
    }

    /**
     * 创建居中布局规则
     *
     * @param parent 父组件
     * @return 布局规则
     */
    public static UILayoutRule centerIn(UIComponent parent) {
        return component -> {
            if (parent == null) {
                return;
            }
            float parentCenterX = parent.getBounds().getCenterX();
            float parentCenterY = parent.getBounds().getCenterY();
            component.setPosition(
                parentCenterX - component.getWidth() / 2f,
                parentCenterY - component.getHeight() / 2f
            );
        };
    }

    /**
     * 创建对齐布局规则
     *
     * @param parent 父组件
     * @param align 对齐方式
     * @return 布局规则
     */
    public static UILayoutRule alignTo(UIComponent parent, UIAlign align) {
        return component -> {
            if (parent == null) {
                return;
            }

            UIRect parentBounds = parent.getBounds();
            float x = parentBounds.getX();
            float y = parentBounds.getY();

            switch (align) {
                case TOP_LEFT:
                    break;
                case TOP_CENTER:
                    x = parentBounds.getCenterX() - component.getWidth() / 2f;
                    break;
                case TOP_RIGHT:
                    x = parentBounds.getRight() - component.getWidth();
                    break;
                case MIDDLE_LEFT:
                    y = parentBounds.getCenterY() - component.getHeight() / 2f;
                    break;
                case CENTER:
                    x = parentBounds.getCenterX() - component.getWidth() / 2f;
                    y = parentBounds.getCenterY() - component.getHeight() / 2f;
                    break;
                case MIDDLE_RIGHT:
                    x = parentBounds.getRight() - component.getWidth();
                    y = parentBounds.getCenterY() - component.getHeight() / 2f;
                    break;
                case BOTTOM_LEFT:
                    y = parentBounds.getBottom() - component.getHeight();
                    break;
                case BOTTOM_CENTER:
                    x = parentBounds.getCenterX() - component.getWidth() / 2f;
                    y = parentBounds.getBottom() - component.getHeight();
                    break;
                case BOTTOM_RIGHT:
                    x = parentBounds.getRight() - component.getWidth();
                    y = parentBounds.getBottom() - component.getHeight();
                    break;
            }

            component.setPosition(x, y);
        };
    }

    /**
     * 创建填充布局规则
     *
     * @param parent 父组件
     * @param padding 内边距
     * @return 布局规则
     */
    public static UILayoutRule fill(UIComponent parent, float padding) {
        return component -> {
            if (parent == null) {
                return;
            }
            UIRect parentBounds = parent.getBounds();
            component.setPosition(
                parentBounds.getX() + padding,
                parentBounds.getY() + padding
            );
            component.setSize(
                parentBounds.getWidth() - padding * 2,
                parentBounds.getHeight() - padding * 2
            );
        };
    }

    /**
     * 创建固定位置规则
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 布局规则
     */
    public static UILayoutRule atPosition(float x, float y) {
        return component -> component.setPosition(x, y);
    }

    /**
     * 创建固定大小规则
     *
     * @param width 宽度
     * @param height 高度
     * @return 布局规则
     */
    public static UILayoutRule withSize(float width, float height) {
        return component -> component.setSize(width, height);
    }
}
