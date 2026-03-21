package io.github.s3s3l.yggdrasil.game.core.flow.scene;

import io.github.s3s3l.yggdrasil.game.core.flow.GameManager;
import io.github.s3s3l.yggdrasil.game.core.flow.GameState;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneManager;
import io.github.s3s3l.yggdrasil.game.core.ui.scene.UIScene;

/**
 * 暂停菜单场景
 * 游戏暂停时显示的菜单
 */
public class PauseMenuScene extends UIScene {

    /**
     * 继续游戏回调
     */
    private Runnable onContinueCallback;

    /**
     * 保存游戏回调
     */
    private Runnable onSaveGameCallback;

    /**
     * 加载游戏回调
     */
    private Runnable onLoadGameCallback;

    /**
     * 返回主菜单回调
     */
    private Runnable onReturnToMainMenuCallback;

    /**
     * 退出游戏回调
     */
    private Runnable onExitCallback;

    /**
     * 创建暂停菜单场景
     */
    public PauseMenuScene() {
        super("PauseMenu");
    }

    /**
     * 设置继续游戏回调
     *
     * @param callback 回调
     * @return this
     */
    public PauseMenuScene setOnContinueCallback(Runnable callback) {
        this.onContinueCallback = callback;
        return this;
    }

    /**
     * 设置保存游戏回调
     *
     * @param callback 回调
     * @return this
     */
    public PauseMenuScene setOnSaveGameCallback(Runnable callback) {
        this.onSaveGameCallback = callback;
        return this;
    }

    /**
     * 设置加载游戏回调
     *
     * @param callback 回调
     * @return this
     */
    public PauseMenuScene setOnLoadGameCallback(Runnable callback) {
        this.onLoadGameCallback = callback;
        return this;
    }

    /**
     * 设置返回主菜单回调
     *
     * @param callback 回调
     * @return this
     */
    public PauseMenuScene setOnReturnToMainMenuCallback(Runnable callback) {
        this.onReturnToMainMenuCallback = callback;
        return this;
    }

    /**
     * 设置退出回调
     *
     * @param callback 回调
     * @return this
     */
    public PauseMenuScene setOnExitCallback(Runnable callback) {
        this.onExitCallback = callback;
        return this;
    }

    @Override
    protected void onEnter() {
        super.onEnter();
        // 进入暂停菜单时更新游戏状态
        if (GameManager.getInstance().isPlaying()) {
            GameManager.getInstance().pauseGame();
        }
    }

    /**
     * 继续游戏
     */
    public void continueGame() {
        if (onContinueCallback != null) {
            onContinueCallback.run();
        } else {
            // 默认行为：恢复游戏并返回游戏场景
            GameManager.getInstance().resumeGame();
        }
    }

    /**
     * 保存游戏
     */
    public void saveGame() {
        if (onSaveGameCallback != null) {
            onSaveGameCallback.run();
        } else {
            // 默认行为：进入存档/读档状态
            GameManager.getInstance().enterSaveLoad();
        }
    }

    /**
     * 加载游戏
     */
    public void loadGame() {
        if (onLoadGameCallback != null) {
            onLoadGameCallback.run();
        } else {
            // 默认行为：进入存档/读档状态
            GameManager.getInstance().enterSaveLoad();
        }
    }

    /**
     * 返回主菜单
     */
    public void returnToMainMenu() {
        if (onReturnToMainMenuCallback != null) {
            onReturnToMainMenuCallback.run();
        } else {
            // 默认行为：结束游戏并返回主菜单
            GameManager.getInstance().endGame();
        }
    }

    /**
     * 退出游戏
     */
    public void exitGame() {
        if (onExitCallback != null) {
            onExitCallback.run();
        }
    }

    /**
     * 保存并退出
     */
    public void saveAndExit() {
        saveGame();
        returnToMainMenu();
    }
}
