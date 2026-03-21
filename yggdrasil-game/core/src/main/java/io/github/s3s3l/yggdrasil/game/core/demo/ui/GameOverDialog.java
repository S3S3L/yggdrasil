package io.github.s3s3l.yggdrasil.game.core.demo.ui;

import io.github.s3s3l.yggdrasil.game.core.ui.UIButton;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIVisibility;

/**
 * 游戏结束对话框
 */
public class GameOverDialog extends UIComponent {

    /**
     * 消息标签
     */
    private UILabel messageLabel;

    /**
     * 分数标签
     */
    private UILabel scoreLabel;

    /**
     * 重新开始按钮
     */
    private UIButton restartButton;

    /**
     * 返回主菜单按钮
     */
    private UIButton menuButton;

    /**
     * 是否胜利
     */
    private boolean isVictory;

    /**
     * 创建游戏结束对话框
     */
    public GameOverDialog() {
        this.isVictory = false;

        // 设置背景
        setBackgroundColor(new UIColor(0, 0, 0, 0.8f));
        setSize(400, 300);

        // 消息标签
        messageLabel = new UILabel();
        messageLabel.setFontSize(24);
        messageLabel.setPosition(0, 100);
        addChild(messageLabel);

        // 分数标签
        scoreLabel = new UILabel();
        scoreLabel.setFontSize(16);
        scoreLabel.setPosition(0, 50);
        addChild(scoreLabel);

        // 重新开始按钮
        restartButton = new UIButton();
        restartButton.setText("重新开始");
        restartButton.setWidth(120);
        restartButton.setHeight(40);
        restartButton.setPosition(-70, -20);
        addChild(restartButton);

        // 返回主菜单按钮
        menuButton = new UIButton();
        menuButton.setText("返回主菜单");
        menuButton.setWidth(120);
        menuButton.setHeight(40);
        menuButton.setPosition(70, -20);
        addChild(menuButton);

        setName("GameOverDialog");
        setVisibility(UIVisibility.HIDDEN);
    }

    /**
     * 显示游戏结束对话框
     *
     * @param isVictory 是否胜利
     * @param score     最终分数
     */
    public void show(boolean isVictory, int score) {
        this.isVictory = isVictory;

        if (isVictory) {
            messageLabel.setText("胜利！");
            messageLabel.setTextColor(new UIColor(0.2f, 0.8f, 0.2f, 1.0f));
        } else {
            messageLabel.setText("失败！");
            messageLabel.setTextColor(new UIColor(0.8f, 0.2f, 0.2f, 1.0f));
        }

        scoreLabel.setText("最终分数：" + score);

        setVisibility(UIVisibility.VISIBLE);
    }

    /**
     * 隐藏对话框
     */
    public void hide() {
        setVisibility(UIVisibility.HIDDEN);
    }

    /**
     * 设置重新开始按钮点击监听器
     *
     * @param listener 监听器
     */
    public void setOnRestartClickListener(Runnable listener) {
        if (restartButton != null) {
            restartButton.setOnClickListener(listener);
        }
    }

    /**
     * 设置返回主菜单按钮点击监听器
     *
     * @param listener 监听器
     */
    public void setOnMenuClickListener(Runnable listener) {
        if (menuButton != null) {
            menuButton.setOnClickListener(listener);
        }
    }

    /**
     * 检查是否胜利
     *
     * @return true 如果胜利
     */
    public boolean isVictory() {
        return isVictory;
    }

    @Override
    public void render() {
        // 由游戏引擎实现渲染
    }
}
