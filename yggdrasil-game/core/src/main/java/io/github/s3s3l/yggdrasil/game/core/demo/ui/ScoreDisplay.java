package io.github.s3s3l.yggdrasil.game.core.demo.ui;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;

/**
 * 分数显示组件
 */
public class ScoreDisplay extends UIComponent {

    /**
     * 分数标签
     */
    private UILabel scoreLabel;

    /**
     * 等级标签
     */
    private UILabel levelLabel;

    /**
     * 当前分数
     */
    private int score;

    /**
     * 当前等级
     */
    private int level;

    /**
     * 创建分数显示
     */
    public ScoreDisplay() {
        this(0, 0);
    }

    /**
     * 创建分数显示
     *
     * @param x X 位置
     * @param y Y 位置
     */
    public ScoreDisplay(float x, float y) {
        this.score = 0;
        this.level = 1;

        setPosition(x, y);

        // 创建分数标签
        scoreLabel = new UILabel();
        scoreLabel.setText("Score: 0");
        scoreLabel.setTextColor(new UIColor(1.0f, 1.0f, 1.0f, 1.0f));
        scoreLabel.setFontSize(16);
        scoreLabel.setPosition(0, 30);
        addChild(scoreLabel);

        // 创建等级标签
        levelLabel = new UILabel();
        levelLabel.setText("Level: 1");
        levelLabel.setTextColor(new UIColor(1.0f, 1.0f, 1.0f, 1.0f));
        levelLabel.setFontSize(16);
        levelLabel.setPosition(0, 0);
        addChild(levelLabel);

        setName("ScoreDisplay");
    }

    /**
     * 设置分数
     *
     * @param score 分数
     */
    public void setScore(int score) {
        this.score = score;
        scoreLabel.setText("Score: " + score);
    }

    /**
     * 获取分数
     *
     * @return 分数
     */
    public int getScore() {
        return score;
    }

    /**
     * 添加分数
     *
     * @param points 分数
     */
    public void addScore(int points) {
        setScore(score + points);
    }

    /**
     * 设置等级
     *
     * @param level 等级
     */
    public void setLevel(int level) {
        this.level = level;
        levelLabel.setText("Level: " + level);
    }

    /**
     * 获取等级
     *
     * @return 等级
     */
    public int getLevel() {
        return level;
    }

    /**
     * 设置颜色
     *
     * @param color 颜色
     */
    public void setColor(UIColor color) {
        if (scoreLabel != null) {
            scoreLabel.setTextColor(color);
        }
        if (levelLabel != null) {
            levelLabel.setTextColor(color);
        }
    }

    @Override
    public void render() {
        // 由游戏引擎实现渲染
    }
}
