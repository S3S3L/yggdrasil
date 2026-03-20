package io.github.s3s3l.yggdrasil.game.gdx.ui.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;

import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.game.core.ui.UICanvas;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIButton;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIInputField;
import io.github.s3s3l.yggdrasil.game.core.ui.UIPanel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIManager;
import io.github.s3s3l.yggdrasil.game.core.ui.UIDirection;
import io.github.s3s3l.yggdrasil.game.core.ui.UILayout;
import io.github.s3s3l.yggdrasil.game.gdx.scene.GdxScene;
import io.github.s3s3l.yggdrasil.game.gdx.ui.renderer.GdxUIRenderer;

/**
 * UI 演示场景
 * 展示如何使用 UI 框架
 */
public class UIDemoScene extends GdxScene {

    private GdxUIRenderer uiRenderer;
    private UICanvas mainCanvas;

    public UIDemoScene(SceneConfig config) {
        super(config);
    }

    @Override
    public void show() {
        super.show();

        // 创建 UI 渲染器
        uiRenderer = new GdxUIRenderer(
            viewport.getWorldWidth(),
            viewport.getWorldHeight(),
            camera
        );

        // 创建主画布
        mainCanvas = createMainCanvas();
        UIManager.getInstance().addCanvas(mainCanvas);
        uiRenderer.syncCanvas(mainCanvas);

        // 设置输入处理器
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiRenderer.getStage());
        multiplexer.addProcessor(new UIInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void hide() {
        super.hide();
        UIManager.getInstance().getCanvases().clear();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 更新和渲染 UI
        uiRenderer.update(delta);
        uiRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (uiRenderer != null) {
            uiRenderer.onResize(width, height);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (uiRenderer != null) {
            uiRenderer.dispose();
        }
    }

    @Override
    protected void doCalculate(float delta) {
        // UI 更新在 render 中处理
    }

    @Override
    protected void doPreRender() {
        // 无需预处理
    }

    @Override
    protected void doSnapshot(io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot snapshot) {
        // 无需快照
    }

    @Override
    protected void doRestore(io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot snapshot) {
        // 无需恢复
    }

    @Override
    public void afterPropertiesSet() {
        // 初始化
    }

    @Override
    public void onWindowResize(int width, int height) {
        resize(width, height);
    }

    /**
     * 创建主画布
     */
    private UICanvas createMainCanvas() {
        UICanvas canvas = new UICanvas("MainCanvas");
        canvas.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        canvas.setBackgroundColor(new UIColor(0, 0, 0, 0.5f));

        // 创建标题标签
        UILabel titleLabel = new UILabel("UI Framework Demo");
        titleLabel.setTextColor(UIColor.WHITE);
        titleLabel.setFontSize(32);
        titleLabel.setSize(400, 50);
        canvas.addChild(titleLabel);

        // 应用居中布局
        UILayout titleLayout = new UILayout();
        titleLayout.addRule(UILayout.centerIn(canvas));
        titleLayout.apply(titleLabel);

        // 创建按钮面板
        UIPanel buttonPanel = createButtonPanel();
        buttonPanel.setSize(300, 200);
        canvas.addChild(buttonPanel);

        // 创建输入框面板
        UIPanel inputPanel = createInputPanel();
        inputPanel.setSize(300, 100);
        canvas.addChild(inputPanel);

        return canvas;
    }

    /**
     * 创建按钮面板
     */
    private UIPanel createButtonPanel() {
        UIPanel panel = new UIPanel("ButtonPanel");
        panel.setBackgroundColor(new UIColor(0.2f, 0.2f, 0.3f, 0.8f));
        panel.setSpacing(10);

        // 添加按钮
        UIButton button1 = new UIButton("Button 1");
        button1.setSize(200, 40);
        button1.setBackgroundColor(new UIColor(0.2f, 0.6f, 0.2f, 1f));
        button1.setTextColor(UIColor.WHITE);
        button1.setOnClickListener(() -> {
            System.out.println("Button 1 clicked!");
        });
        panel.addChild(button1);

        UIButton button2 = new UIButton("Button 2");
        button2.setSize(200, 40);
        button2.setBackgroundColor(new UIColor(0.6f, 0.2f, 0.2f, 1f));
        button2.setTextColor(UIColor.WHITE);
        button2.setOnClickListener(() -> {
            System.out.println("Button 2 clicked!");
        });
        panel.addChild(button2);

        return panel;
    }

    /**
     * 创建输入面板
     */
    private UIPanel createInputPanel() {
        UIPanel panel = new UIPanel("InputPanel");
        panel.setBackgroundColor(new UIColor(0.2f, 0.3f, 0.2f, 0.8f));
        panel.setSpacing(10);

        // 添加输入框
        UIInputField inputField = new UIInputField();
        inputField.setText("Enter text...");
        inputField.setSize(200, 40);
        inputField.setTextColor(UIColor.WHITE);
        inputField.setBackgroundColor(new UIColor(0.1f, 0.1f, 0.15f, 1f));
        panel.addChild(inputField);

        return panel;
    }

    /**
     * UI 输入处理器
     */
    private class UIInputProcessor extends InputAdapter {

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (uiRenderer != null) {
                return uiRenderer.handleTouchDown(screenX, screenY, pointer, button);
            }
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (uiRenderer != null) {
                uiRenderer.handleHover(screenX, screenY);
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            if (uiRenderer != null) {
                uiRenderer.handleHover(screenX, screenY);
            }
            return false;
        }

        @Override
        public boolean keyDown(int keycode) {
            if (uiRenderer != null) {
                return uiRenderer.handleKeyPress(keycode);
            }
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            if (uiRenderer != null) {
                return uiRenderer.handleTextInput(String.valueOf(character));
            }
            return false;
        }
    }
}
