package io.github.s3s3l.yggdrasil.game.gdx.ui.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.s3s3l.yggdrasil.game.core.ui.UICanvas;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIManager;

/**
 * LibGDX UI 渲染器
 * 将核心 UI 组件渲染到 LibGDX Stage
 */
public class GdxUIRenderer implements Disposable {

    private final Stage stage;
    private final Viewport viewport;
    private final Vector3 unprojectTarget;
    private boolean enabled;

    public GdxUIRenderer(float width, float height, Camera camera) {
        this.viewport = new FitViewport(width, height, camera);
        this.stage = new Stage(this.viewport);
        this.unprojectTarget = new Vector3();
        this.enabled = true;
    }

    public GdxUIRenderer(float width, float height) {
        this.viewport = new FitViewport(width, height);
        this.stage = new Stage(this.viewport);
        this.unprojectTarget = new Vector3();
        this.enabled = true;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 更新 UI
     *
     * @param deltaTime 时间增量
     */
    public void update(float deltaTime) {
        if (!enabled) {
            return;
        }
        UIManager.getInstance().update(deltaTime);
    }

    /**
     * 渲染 UI
     */
    public void render() {
        if (!enabled) {
            return;
        }
        stage.act();
        stage.draw();
    }

    /**
     * 处理屏幕坐标点击
     *
     * @param screenX 屏幕 X 坐标
     * @param screenY 屏幕 Y 坐标
     * @param pointer 指针索引
     * @param button  按钮代码
     * @return 是否处理了点击
     */
    public boolean handleTouchDown(int screenX, int screenY, int pointer, int button) {
        if (!enabled) {
            return false;
        }
        // 将屏幕坐标转换为舞台坐标
        unprojectTarget.set(screenX, screenY, 0);
        viewport.unproject(unprojectTarget);
        return stage.touchDown((int) unprojectTarget.x, (int) unprojectTarget.y, pointer, button);
    }

    /**
     * 处理悬停
     *
     * @param screenX 屏幕 X 坐标
     * @param screenY 屏幕 Y 坐标
     */
    public void handleHover(int screenX, int screenY) {
        if (!enabled) {
            return;
        }
        unprojectTarget.set(screenX, screenY, 0);
        viewport.unproject(unprojectTarget);
        UIManager.getInstance().handleHover(unprojectTarget.x, unprojectTarget.y);
    }

    /**
     * 处理文本输入
     *
     * @param text 输入的文本
     * @return 是否处理了输入
     */
    public boolean handleTextInput(String text) {
        if (!enabled) {
            return false;
        }
        return UIManager.getInstance().handleTextInput(text);
    }

    /**
     * 处理按键
     *
     * @param keyCode 按键代码
     * @return 是否处理了按键
     */
    public boolean handleKeyPress(int keyCode) {
        if (!enabled) {
            return false;
        }
        return UIManager.getInstance().handleKeyPress(keyCode);
    }

    /**
     * 同步核心 UI 画布到 Scene2D
     *
     * @param canvas 核心 UI 画布
     */
    public void syncCanvas(UICanvas canvas) {
        if (canvas == null) {
            return;
        }

        // 清除旧的 actor
        stage.clear();

        // 同步子组件
        for (UIComponent child : canvas.getChildren()) {
            Actor actor = UIComponentSync.syncComponent(child);
            if (actor != null) {
                stage.addActor(actor);
            }
        }

        // 同步 overlays
        for (UIComponent overlay : canvas.getOverlays()) {
            Actor actor = UIComponentSync.syncComponent(overlay);
            if (actor != null) {
                stage.addActor(actor);
            }
        }
    }

    /**
     * 从 UIColor 创建 Drawable
     *
     * @param color UI 颜色
     * @return Scene2D Drawable
     */
    public Drawable createDrawable(UIColor color) {
        if (color == null) {
            return null;
        }
        Color gdxColor = getColor(color);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(gdxColor);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(texture);
    }

    /**
     * 将 UIColor 转换为 Color
     *
     * @param color UI 颜色
     * @return LibGDX Color
     */
    public static Color getColor(UIColor color) {
        if (color == null) {
            return Color.WHITE;
        }
        return new Color(color.getR(), color.getG(), color.getB(), color.getA());
    }

    /**
     * 获取默认字体
     *
     * @return BitmapFont
     */
    public static BitmapFont getFont() {
        return new BitmapFont();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * 处理窗口大小变化
     *
     * @param width  宽度
     * @param height 高度
     */
    public void onResize(int width, int height) {
        viewport.update(width, height, true);
    }
}
