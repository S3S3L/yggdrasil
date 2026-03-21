package io.github.s3s3l.yggdrasil.game.core.demo.ui;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIProgressBar;

/**
 * 敌人血条
 * 显示在敌人上方的血条
 */
public class EnemyHealthBar extends UIComponent {

    /**
     * 生命值进度条
     */
    private UIProgressBar healthBar;

    /**
     * 敌人名称标签
     */
    private UILabel nameLabel;

    /**
     * 创建敌人血条
     */
    public EnemyHealthBar() {
        this(80, 8);
    }

    /**
     * 创建敌人血条
     *
     * @param width  宽度
     * @param height 高度
     */
    public EnemyHealthBar(float width, float height) {
        // 设置背景
        setBackgroundColor(new UIColor(0.2f, 0.2f, 0.2f, 0.8f));
        setSize(width, height);

        // 创建生命值进度条
        healthBar = new UIProgressBar(1.0f, 0.0f, 1.0f);
        healthBar.setWidth(width - 2);
        healthBar.setHeight(height - 2);
        healthBar.setPosition(1, 1);
        healthBar.setFillColor(new UIColor(0.8f, 0.2f, 0.2f, 1.0f));
        addChild(healthBar);

        // 创建名称标签
        nameLabel = new UILabel();
        nameLabel.setText("Enemy");
        nameLabel.setTextColor(new UIColor(1.0f, 1.0f, 1.0f, 1.0f));
        nameLabel.setFontSize(10);
        nameLabel.setPosition(0, height + 2);
        addChild(nameLabel);

        setName("EnemyHealthBar");
    }

    /**
     * 设置生命值
     *
     * @param current 当前生命值
     * @param max     最大生命值
     */
    public void setHealth(float current, float max) {
        if (max <= 0) {
            healthBar.setValue(0);
            return;
        }

        float percent = Math.max(0, Math.min(1, current / max));
        healthBar.setValue(percent);
    }

    /**
     * 设置敌人名称
     *
     * @param name 名称
     */
    public void setEnemyName(String name) {
        if (nameLabel != null) {
            nameLabel.setText(name != null ? name : "Enemy");
        }
    }

    /**
     * 获取生命值百分比
     *
     * @return 生命值百分比
     */
    public float getHealthPercent() {
        return healthBar != null ? healthBar.getValue() : 0;
    }

    @Override
    public void render() {
        // 由游戏引擎实现渲染
    }
}
