package io.github.s3s3l.yggdrasil.game.core.flow.scene;

import io.github.s3s3l.yggdrasil.game.core.flow.GameManager;
import io.github.s3s3l.yggdrasil.game.core.flow.GameState;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneManager;
import io.github.s3s3l.yggdrasil.game.core.ui.scene.UIScene;

/**
 * 主菜单场景
 * 游戏的主菜单界面
 */
public class MainMenuScene extends UIScene {

    /**
     * 开始游戏按钮回调
     */
    private Runnable onStartGameCallback;

    /**
     * 加载游戏按钮回调
     */
    private Runnable onLoadGameCallback;

    /**
     * 设置按钮回调
     */
    private Runnable onSettingsCallback;

    /**
     * 退出游戏按钮回调
     */
    private Runnable onExitCallback;

    /**
     * 创建主菜单场景
     */
    public MainMenuScene() {
        super("MainMenu");
    }

    /**
     * 设置开始游戏回调
     *
     * @param callback 回调
     * @return this
     */
    public MainMenuScene setOnStartGameCallback(Runnable callback) {
        this.onStartGameCallback = callback;
        return this;
    }

    /**
     * 设置加载游戏回调
     *
     * @param callback 回调
     * @return this
     */
    public MainMenuScene setOnLoadGameCallback(Runnable callback) {
        this.onLoadGameCallback = callback;
        return this;
    }

    /**
     * 设置设置回调
     *
     * @param callback 回调
     * @return this
     */
    public MainMenuScene setOnSettingsCallback(Runnable callback) {
        this.onSettingsCallback = callback;
        return this;
    }

    /**
     * 设置退出回调
     *
     * @param callback 回调
     * @return this
     */
    public MainMenuScene setOnExitCallback(Runnable callback) {
        this.onExitCallback = callback;
        return this;
    }

    @Override
    protected void onEnter() {
        super.onEnter();
        // 进入主菜单时更新游戏状态
        GameManager.getInstance().enterMainMenu();
    }

    /**
     * 开始游戏
     */
    public void startGame() {
        if (onStartGameCallback != null) {
            onStartGameCallback.run();
        } else {
            // 默认行为：直接进入游戏
            GameManager.getInstance().startGame();
        }
    }

    /**
     * 加载游戏
     */
    public void loadGame() {
        if (onLoadGameCallback != null) {
            onLoadGameCallback.run();
        } else {
            // 默认行为：进入存档/读档菜单
            GameManager.getInstance().enterSaveLoad();
        }
    }

    /**
     * 打开设置
     */
    public void openSettings() {
        if (onSettingsCallback != null) {
            onSettingsCallback.run();
        } else {
            // 默认行为：进入设置状态
            GameManager.getInstance().enterSettings();
        }
    }

    /**
     * 退出游戏
     */
    public void exitGame() {
        if (onExitCallback != null) {
            onExitCallback.run();
        }
        // 注意：实际退出由平台处理
    }

    /**
     * 继续游戏（从暂停状态返回）
     */
    public void continueGame() {
        GameManager.getInstance().resumeGame();
    }
}
