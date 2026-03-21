package io.github.s3s3l.yggdrasil.game.core.demo.scene;

import io.github.s3s3l.yggdrasil.game.core.demo.config.DemoConfig;
import io.github.s3s3l.yggdrasil.game.core.demo.ui.GameOverDialog;
import io.github.s3s3l.yggdrasil.game.core.flow.scene.GameScene;
import io.github.s3s3l.yggdrasil.game.core.ui.UIButton;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneManager;

/**
 * 开始菜单场景
 */
public class StartMenuScene extends GameScene {

    private UILabel titleLabel;
    private UIButton startButton;
    private UIButton exitButton;

    public StartMenuScene() {
        super("StartMenu");
    }

    @Override
    protected void onEnter() {
        super.onEnter();

        // 创建标题
        titleLabel = new UILabel();
        titleLabel.setText("YGGDRASIL DEMO");
        titleLabel.setFontSize(36);
        titleLabel.setTextColor(new UIColor(0.2f, 0.8f, 0.8f, 1.0f));
        titleLabel.setPosition(0, 150);
        addComponent(titleLabel);

        // 创建开始按钮
        startButton = new UIButton();
        startButton.setText("开始游戏");
        startButton.setWidth(200);
        startButton.setHeight(50);
        startButton.setPosition(0, 50);
        startButton.setOnClickListener(this::onStartClick);
        addComponent(startButton);

        // 创建退出按钮
        exitButton = new UIButton();
        exitButton.setText("退出");
        exitButton.setWidth(200);
        exitButton.setHeight(50);
        exitButton.setPosition(0, -20);
        exitButton.setOnClickListener(this::onExitClick);
        addComponent(exitButton);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    private void onStartClick() {
        // 切换到游戏场景
        DemoGameScene gameScene = new DemoGameScene(120);
        SceneManager.get().replaceScene((io.github.s3s3l.yggdrasil.game.core.scene.Scene) gameScene);
    }

    private void onExitClick() {
        // 退出游戏
        System.exit(0);
    }

    @Override
    public void render() {
        // 渲染 UI
    }
}
