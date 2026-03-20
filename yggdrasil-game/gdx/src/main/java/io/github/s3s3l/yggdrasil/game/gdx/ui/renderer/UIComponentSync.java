package io.github.s3s3l.yggdrasil.game.gdx.ui.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import io.github.s3s3l.yggdrasil.game.core.ui.UIButton;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UIInputField;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIPanel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIProgressBar;
import io.github.s3s3l.yggdrasil.game.core.ui.UIScrollView;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIDirection;
import io.github.s3s3l.yggdrasil.game.core.ui.UIVisibility;

/**
 * UI 组件同步器
 * 负责将核心 UI 组件同步到 LibGDX Scene2D 组件
 */
public class UIComponentSync {

    /**
     * 同步按钮
     *
     * @param button 核心 UI 按钮
     * @return Scene2D TextButton
     */
    public static TextButton syncButton(UIButton button) {
        TextButtonStyle style = new TextButtonStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(button.getTextColor());

        if (button.getBackgroundColor() != null) {
            style.up = createDrawable(button.getBackgroundColor());
            style.down = createDrawable(button.getBackgroundColor().darken(0.2f));
            style.over = createDrawable(button.getBackgroundColor().lighten(0.1f));
        }

        TextButton textButton = new TextButton(button.getText(), style);
        textButton.setSize(button.getWidth(), button.getHeight());
        textButton.setDisabled(!button.isEnabled() || !button.isInteractable());

        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                button.onClick();
            }
        });

        // 同步悬停状态
        textButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.setHovered(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.setHovered(false);
            }
        });

        return textButton;
    }

    /**
     * 同步标签
     *
     * @param label 核心 UI 标签
     * @return Scene2D Label
     */
    public static Label syncLabel(UILabel label) {
        LabelStyle style = new LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(label.getTextColor());

        Label textLabel = new Label(label.getText(), style);
        textLabel.setSize(label.getWidth(), label.getHeight());
        textLabel.setWrap(label.isWrap());

        return textLabel;
    }

    /**
     * 同步输入框
     *
     * @param inputField 核心 UI 输入框
     * @return Scene2D TextField
     */
    public static TextField syncInputField(UIInputField inputField) {
        TextFieldStyle style = new TextFieldStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(inputField.getTextColor());

        UIColor bgColor = inputField.getBackgroundColor() != null
                ? inputField.getBackgroundColor()
                : new UIColor(0.2f, 0.2f, 0.2f, 1f);
        style.background = createDrawable(bgColor);
        style.cursor = createDrawable(new UIColor(1f, 1f, 1f, 1f));
        style.selection = createDrawable(new UIColor(0.3f, 0.5f, 0.8f, 0.5f));

        TextField textField = new TextField(inputField.getText(), style);
        textField.setSize(inputField.getWidth(), inputField.getHeight());
        textField.setDisabled(!inputField.isEnabled() || !inputField.isInteractable());

        textField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inputField.handleInput(textField.getText());
            }
        });

        return textField;
    }

    /**
     * 同步面板
     *
     * @param panel 核心 UI 面板
     * @return Scene2D Table
     */
    public static Table syncPanel(UIPanel panel) {
        Table table = new Table();
        table.setSize(panel.getWidth(), panel.getHeight());

        if (panel.getBackgroundColor() != null) {
            table.setBackground(createDrawable(panel.getBackgroundColor()));
        }

        // 根据布局方向设置
        UIDirection direction = panel.getLayoutDirection();
        if (direction == UIDirection.VERTICAL) {
            table.defaults().width(panel.getWidth()).pad(panel.getSpacing());
        } else {
            table.defaults().height(panel.getHeight()).pad(panel.getSpacing());
        }

        // 添加子组件
        for (UIComponent child : panel.getChildren()) {
            Actor actor = syncComponent(child);
            if (actor != null) {
                table.add(actor);
                if (direction == UIDirection.VERTICAL) {
                    table.row();
                }
            }
        }

        return table;
    }

    /**
     * 同步进度条
     *
     * @param progressBar 核心 UI 进度条
     * @return Scene2D Touchpad (用旋钮位置模拟进度)
     */
    public static Touchpad syncProgressBar(UIProgressBar progressBar) {
        TouchpadStyle style = new TouchpadStyle();
        UIColor fillColor = progressBar.getFillColor() != null
                ? progressBar.getFillColor()
                : new UIColor(0.5f, 0.5f, 1f, 1f);
        style.knob = createDrawable(fillColor);
        style.background = createDrawable(progressBar.getBackgroundColor() != null
                ? progressBar.getBackgroundColor()
                : new UIColor(0.2f, 0.2f, 0.2f, 1f));

        Touchpad touchpad = new Touchpad(0, style);
        touchpad.setSize(progressBar.getWidth(), progressBar.getHeight());
        // touchpad.setDisabled(!progressBar.isEnabled());

        return touchpad;
    }

    /**
     * 同步滚动视图
     *
     * @param scrollView 核心 UI 滚动视图
     * @return Scene2D ScrollPane
     */
    public static ScrollPane syncScrollView(UIScrollView scrollView) {
        Table contentTable = new Table();

        // 添加子组件
        for (UIComponent child : scrollView.getChildren()) {
            Actor actor = syncComponent(child);
            if (actor != null) {
                contentTable.add(actor).top().left();
                contentTable.row();
            }
        }

        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.setSize(scrollView.getWidth(), scrollView.getHeight());

        // 设置滚动
        scrollPane.setScrollingDisabled(
                !scrollView.isHorizontalScrollEnabled(),
                !scrollView.isVerticalScrollEnabled());

        return scrollPane;
    }

    /**
     * 同步通用组件
     *
     * @param component 核心 UI 组件
     * @return Scene2D Actor
     */
    public static Actor syncGenericComponent(UIComponent component) {
        Table table = new Table();
        table.setSize(component.getWidth(), component.getHeight());

        if (component.getBackgroundColor() != null) {
            table.setBackground(createDrawable(component.getBackgroundColor()));
        }

        return table;
    }

    /**
     * 同步组件（分发到具体方法）
     *
     * @param component 核心 UI 组件
     * @return Scene2D Actor
     */
    public static Actor syncComponent(UIComponent component) {
        if (component == null || component.getVisibility() == UIVisibility.HIDDEN) {
            return null;
        }

        if (component instanceof UIButton) {
            return syncButton((UIButton) component);
        } else if (component instanceof UILabel) {
            return syncLabel((UILabel) component);
        } else if (component instanceof UIInputField) {
            return syncInputField((UIInputField) component);
        } else if (component instanceof UIPanel) {
            return syncPanel((UIPanel) component);
        } else if (component instanceof UIProgressBar) {
            return syncProgressBar((UIProgressBar) component);
        } else if (component instanceof UIScrollView) {
            return syncScrollView((UIScrollView) component);
        } else {
            return syncGenericComponent(component);
        }
    }

    /**
     * 从 UIColor 创建 Drawable
     *
     * @param color UI 颜色
     * @return Scene2D Drawable
     */
    private static Drawable createDrawable(UIColor color) {
        if (color == null) {
            return null;
        }
        Color gdxColor = GdxUIRenderer.getColor(color);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(gdxColor);
        pixmap.fillRectangle(0, 0, 1, 1);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(texture);
    }
}
