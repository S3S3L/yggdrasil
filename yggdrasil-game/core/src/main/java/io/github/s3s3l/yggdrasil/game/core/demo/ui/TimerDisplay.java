package io.github.s3s3l.yggdrasil.game.core.demo.ui;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;

/**
 * 计时器显示组件
 * 显示游戏生存时间
 */
public class TimerDisplay extends UIComponent {

    /**
     * 时间标签
     */
    private UILabel timerLabel;

    /**
     * 当前时间（秒）
     */
    private float currentTime;

    /**
     * 目标时间（秒）
     */
    private float goalTime;

    /**
     * 创建计时器显示
     */
    public TimerDisplay() {
        this(0, 0, 120);
    }

    /**
     * 创建计时器显示
     *
     * @param x        X 位置
     * @param y        Y 位置
     * @param goalTime 目标时间（秒）
     */
    public TimerDisplay(float x, float y, float goalTime) {
        this.currentTime = 0;
        this.goalTime = goalTime;

        setPosition(x, y);

        // 创建时间标签
        timerLabel = new UILabel();
        timerLabel.setText(formatTime(0));
        timerLabel.setTextColor(new UIColor(1.0f, 1.0f, 1.0f, 1.0f));
        timerLabel.setFontSize(18);
        addChild(timerLabel);

        setName("TimerDisplay");
    }

    /**
     * 格式化时间为 MM:SS 格式
     *
     * @param seconds 秒数
     * @return 格式化后的时间字符串
     */
    public String formatTime(float seconds) {
        int totalSeconds = (int) Math.max(0, seconds);
        int minutes = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    /**
     * 设置当前时间
     *
     * @param time 时间（秒）
     */
    public void setTime(float time) {
        this.currentTime = time;
        timerLabel.setText(formatTime(time) + " / " + formatTime(goalTime));

        // 当时间接近目标时改变颜色
        float remaining = goalTime - time;
        if (remaining <= 10) {
            timerLabel.setTextColor(new UIColor(0.2f, 0.8f, 0.2f, 1.0f)); // 绿色
        } else if (remaining <= 30) {
            timerLabel.setTextColor(new UIColor(0.8f, 0.8f, 0.2f, 1.0f)); // 黄色
        } else {
            timerLabel.setTextColor(new UIColor(1.0f, 1.0f, 1.0f, 1.0f)); // 白色
        }
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间（秒）
     */
    public float getCurrentTime() {
        return currentTime;
    }

    /**
     * 获取目标时间
     *
     * @return 目标时间（秒）
     */
    public float getGoalTime() {
        return goalTime;
    }

    @Override
    public void render() {
        // 由游戏引擎实现渲染
    }
}
