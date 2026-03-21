package io.github.s3s3l.yggdrasil.game.core.demo.ui;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIProgressBar;

/**
 * 血条组件
 * 显示实体生命值
 */
public class HealthBar extends UIComponent {

    /**
     * 生命值进度条
     */
    private UIProgressBar healthBar;

    /**
     * 生命值文本
     */
    private UILabel healthText;

    /**
     * 当前生命值
     */
    private float currentHealth;

    /**
     * 最大生命值
     */
    private float maxHealth;

    /**
     * 创建血条
     */
    public HealthBar() {
        this(100, 100);
    }

    /**
     * 创建血条
     *
     * @param width     宽度
     * @param height    高度
     */
    public HealthBar(float width, float height) {
        this.currentHealth = 100;
        this.maxHealth = 100;

        // 设置背景颜色
        setBackgroundColor(new UIColor(0.2f, 0.2f, 0.2f, 0.8f));
        setSize(width, height);

        // 创建生命值进度条
        healthBar = new UIProgressBar(1.0f, 0.0f, 1.0f);
        healthBar.setWidth(width - 4);
        healthBar.setHeight(height - 4);
        healthBar.setPosition(2, 2);
        healthBar.setFillColor(new UIColor(0.2f, 0.8f, 0.2f, 1.0f));
        addChild(healthBar);

        // 创建生命值文本
        healthText = new UILabel();
        healthText.setText("100/100");
        healthText.setTextColor(new UIColor(1.0f, 1.0f, 1.0f, 1.0f));
        healthText.setFontSize(12);
        addChild(healthText);

        setName("HealthBar");
    }

    /**
     * 设置生命值
     *
     * @param current 当前生命值
     * @param max     最大生命值
     */
    public void setHealth(float current, float max) {
        this.currentHealth = Math.max(0, current);
        this.maxHealth = Math.max(1, max);

        float percent = currentHealth / maxHealth;
        healthBar.setValue(percent);
        healthText.setText(String.format("%.0f/%.0f", currentHealth, maxHealth));

        // 根据生命值百分比改变颜色
        if (percent > 0.5f) {
            healthBar.setFillColor(new UIColor(0.2f, 0.8f, 0.2f, 1.0f));
        } else if (percent > 0.25f) {
            healthBar.setFillColor(new UIColor(0.8f, 0.8f, 0.2f, 1.0f));
        } else {
            healthBar.setFillColor(new UIColor(0.8f, 0.2f, 0.2f, 1.0f));
        }
    }

    /**
     * 获取当前生命值
     *
     * @return 当前生命值
     */
    public float getCurrentHealth() {
        return currentHealth;
    }

    /**
     * 获取最大生命值
     *
     * @return 最大生命值
     */
    public float getMaxHealth() {
        return maxHealth;
    }

    /**
     * 获取生命值百分比
     *
     * @return 生命值百分比
     */
    public float getHealthPercent() {
        return maxHealth > 0 ? currentHealth / maxHealth : 0;
    }

    /**
     * 设置文本位置
     *
     * @param x X 位置
     * @param y Y 位置
     */
    public void setHealthTextPosition(float x, float y) {
        if (healthText != null) {
            healthText.setPosition(x, y);
        }
    }

    @Override
    public void render() {
        // 由游戏引擎实现渲染
    }
}
