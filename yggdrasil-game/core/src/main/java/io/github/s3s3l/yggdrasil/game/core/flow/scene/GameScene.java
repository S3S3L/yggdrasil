package io.github.s3s3l.yggdrasil.game.core.flow.scene;

import io.github.s3s3l.yggdrasil.game.core.flow.GameManager;
import io.github.s3s3l.yggdrasil.game.core.flow.GameState;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneManager;
import io.github.s3s3l.yggdrasil.game.core.ui.scene.UIScene;

/**
 * 游戏内场景
 * 主要游戏逻辑运行的场景
 */
public class GameScene extends UIScene {

    /**
     * 游戏时间（秒）
     */
    private float gameTime;

    /**
     * 游戏速度
     */
    private float timeScale = 1.0f;

    /**
     * 暂停回调
     */
    private Runnable onPauseCallback;

    /**
     * 游戏结束回调
     */
    private Runnable onGameOverCallback;

    /**
     * 胜利回调
     */
    private Runnable onVictoryCallback;

    /**
     * 失败回调
     */
    private Runnable onDefeatCallback;

    /**
     * 创建游戏场景
     */
    public GameScene() {
        super("Game");
        this.gameTime = 0;
    }

    /**
     * 创建游戏场景
     *
     * @param name 场景名称
     */
    public GameScene(String name) {
        super(name);
        this.gameTime = 0;
    }

    /**
     * 设置暂停回调
     *
     * @param callback 回调
     * @return this
     */
    public GameScene setOnPauseCallback(Runnable callback) {
        this.onPauseCallback = callback;
        return this;
    }

    /**
     * 设置游戏结束回调
     *
     * @param callback 回调
     * @return this
     */
    public GameScene setOnGameOverCallback(Runnable callback) {
        this.onGameOverCallback = callback;
        return this;
    }

    /**
     * 设置胜利回调
     *
     * @param callback 回调
     * @return this
     */
    public GameScene setOnVictoryCallback(Runnable callback) {
        this.onVictoryCallback = callback;
        return this;
    }

    /**
     * 设置失败回调
     *
     * @param callback 回调
     * @return this
     */
    public GameScene setOnDefeatCallback(Runnable callback) {
        this.onDefeatCallback = callback;
        return this;
    }

    /**
     * 获取游戏时间
     *
     * @return 游戏时间（秒）
     */
    public float getGameTime() {
        return gameTime;
    }

    /**
     * 设置游戏时间
     *
     * @param gameTime 游戏时间（秒）
     */
    public void setGameTime(float gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * 获取时间缩放
     *
     * @return 时间缩放
     */
    public float getTimeScale() {
        return timeScale;
    }

    /**
     * 设置时间缩放
     *
     * @param timeScale 时间缩放（0-1 之间暂停）
     */
    public void setTimeScale(float timeScale) {
        this.timeScale = Math.max(0, timeScale);
    }

    @Override
    protected void onEnter() {
        super.onEnter();
        GameManager.getInstance().startGame();
    }

    @Override
    public void update(float deltaTime) {
        if (!isActive() || isPaused()) {
            return;
        }

        // 更新游戏时间
        gameTime += deltaTime * timeScale;

        super.update(deltaTime);
    }

    /**
     * 暂停游戏
     */
    public void pause() {
        if (onPauseCallback != null) {
            onPauseCallback.run();
        } else {
            // 默认行为：暂停游戏并显示暂停菜单
            GameManager.getInstance().pauseGame();
            setPaused(true);
        }
    }

    /**
     * 恢复游戏
     */
    public void resume() {
        GameManager.getInstance().resumeGame();
    }

    /**
     * 游戏胜利
     */
    public void victory() {
        if (onVictoryCallback != null) {
            onVictoryCallback.run();
        } else {
            GameManager.getInstance().victory();
        }
    }

    /**
     * 游戏失败
     */
    public void defeat() {
        if (onDefeatCallback != null) {
            onDefeatCallback.run();
        } else {
            GameManager.getInstance().defeat();
        }
    }

    /**
     * 游戏结束
     */
    public void gameOver() {
        if (onGameOverCallback != null) {
            onGameOverCallback.run();
        } else {
            GameManager.getInstance().endGame();
        }
    }
}
