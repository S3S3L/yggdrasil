package io.github.s3s3l.yggdrasil.game.gdx.ui.renderer;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import io.github.s3s3l.yggdrasil.game.core.ui.UIButton;
import io.github.s3s3l.yggdrasil.game.core.ui.UICheckbox;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UIDropdown;
import io.github.s3s3l.yggdrasil.game.core.ui.UIImage;
import io.github.s3s3l.yggdrasil.game.core.ui.UIInputField;
import io.github.s3s3l.yggdrasil.game.core.ui.UIList;
import io.github.s3s3l.yggdrasil.game.core.ui.UILoadingIndicator;
import io.github.s3s3l.yggdrasil.game.core.ui.UIListItem;
import io.github.s3s3l.yggdrasil.game.core.ui.UILabel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIPanel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIProgressBar;
import io.github.s3s3l.yggdrasil.game.core.ui.UIScrollView;
import io.github.s3s3l.yggdrasil.game.core.ui.UISlider;
import io.github.s3s3l.yggdrasil.game.core.ui.UITabContainer;
import io.github.s3s3l.yggdrasil.game.core.ui.UIToggle;
import io.github.s3s3l.yggdrasil.game.core.ui.UITooltip;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIDirection;
import io.github.s3s3l.yggdrasil.game.core.ui.UIVisibility;
import io.github.s3s3l.yggdrasil.game.core.ui.UITreeList;
import io.github.s3s3l.yggdrasil.game.core.ui.UITreeNode;
import io.github.s3s3l.yggdrasil.game.core.ui.UITable;
import io.github.s3s3l.yggdrasil.game.core.ui.UITableColumn;
import io.github.s3s3l.yggdrasil.game.core.ui.UITableCell;
import io.github.s3s3l.yggdrasil.game.core.ui.UIGrid;
import io.github.s3s3l.yggdrasil.game.core.ui.UIGridCell;
import io.github.s3s3l.yggdrasil.game.core.ui.UISplitPanel;
import io.github.s3s3l.yggdrasil.game.core.ui.UIModalDialog;
import io.github.s3s3l.yggdrasil.game.core.ui.UIComboBox;
import io.github.s3s3l.yggdrasil.game.core.ui.UINumberPicker;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColorPicker;
import io.github.s3s3l.yggdrasil.game.core.ui.UIMenuBar;
import io.github.s3s3l.yggdrasil.game.core.ui.UIMenuItem;
import io.github.s3s3l.yggdrasil.game.core.ui.UIContextMenu;
import io.github.s3s3l.yggdrasil.game.core.ui.UIStatusBar;
import io.github.s3s3l.yggdrasil.game.core.ui.UIStatusBarLayer;
import io.github.s3s3l.yggdrasil.game.core.ui.UIVirtualJoystick;

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
     * 同步图片组件
     *
     * @param image 核心 UI 图片组件
     * @return Scene2D Image
     */
    public static Image syncImage(UIImage image) {
        Image gdxImage;

        if (image.getTextureRegion() != null) {
            // 假设 textureRegion 是 TextureRegion 类型
            gdxImage = new Image(new TextureRegionDrawable((com.badlogic.gdx.graphics.g2d.TextureRegion) image.getTextureRegion()));
        } else {
            // 如果没有纹理，使用背景色创建
            UIColor bgColor = image.getBackgroundColor();
            if (bgColor != null && bgColor.getA() > 0) {
                gdxImage = new Image(createDrawable(bgColor));
            } else {
                gdxImage = new Image();
            }
        }

        gdxImage.setSize(image.getWidth(), image.getHeight());
        gdxImage.setColor(GdxUIRenderer.getColor(image.getColorTint()));

        // 根据图片类型设置缩放方式
        switch (image.getImageType()) {
            case STRETCH:
                gdxImage.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
                break;
            case TILED:
                gdxImage.setScaling(com.badlogic.gdx.utils.Scaling.fit);
                break;
            case NINE_PATCH:
                // 九宫格需要特殊处理，这里使用拉伸作为默认
                gdxImage.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
                break;
        }

        if (image.isPreserveAspectRatio()) {
            gdxImage.setScaling(com.badlogic.gdx.utils.Scaling.fit);
        }

        return gdxImage;
    }

    /**
     * 同步多选框组件
     *
     * @param checkbox 核心 UI 多选框组件
     * @return Scene2D CheckBox
     */
    public static CheckBox syncCheckbox(UICheckbox checkbox) {
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(checkbox.getTextColor());
        style.checkboxOn = createDrawable(UIColor.GREEN);
        style.checkboxOff = createDrawable(new UIColor(0.3f, 0.3f, 0.3f, 1f));
        style.checkboxOnOver = createDrawable(UIColor.GREEN.lighten(0.1f));

        CheckBox checkBox = new CheckBox(checkbox.getText(), style);
        checkBox.setSize(checkbox.getWidth(), checkbox.getHeight());
        checkBox.setChecked(checkbox.isChecked());
        checkBox.setDisabled(!checkbox.isEnabled() || !checkbox.isInteractable());

        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkbox.setChecked(checkBox.isChecked());
            }
        });

        return checkBox;
    }

    /**
     * 同步下拉框组件
     *
     * @param dropdown 核心 UI 下拉框组件
     * @return Scene2D SelectBox
     */
    @SuppressWarnings("unchecked")
    public static <T> SelectBox<T> syncDropdown(UIDropdown dropdown) {
        SelectBox.SelectBoxStyle style = new SelectBox.SelectBoxStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(dropdown.getTextColor());
        style.background = createDrawable(dropdown.getDropdownColor());
        style.scrollStyle = new ScrollPane.ScrollPaneStyle();
        style.listStyle = new List.ListStyle();
        style.listStyle.fontColorSelected = GdxUIRenderer.getColor(dropdown.getTextColor());
        style.listStyle.fontColorUnselected = GdxUIRenderer.getColor(dropdown.getTextColor());

        SelectBox<T> selectBox = new SelectBox<>(style);
        selectBox.setSize(dropdown.getWidth(), dropdown.getHeight());

        // 设置选项
        Array<T> items = new Array<>();
        for (String option : dropdown.getOptions()) {
            items.add((T) option);
        }
        selectBox.setItems(items);

        // 设置选中项
        int selectedIndex = dropdown.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < items.size) {
            selectBox.setSelectedIndex(selectedIndex);
        }

        selectBox.setDisabled(!dropdown.isEnabled() || !dropdown.isInteractable());

        // 监听选择变化
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dropdown.setSelectedIndex(selectBox.getSelectedIndex());
            }
        });

        return selectBox;
    }

    /**
     * 同步列表组件
     *
     * @param list 核心 UI 列表组件
     * @return Scene2D ScrollPane (包含 List)
     */
    @SuppressWarnings("unchecked")
    public static ScrollPane syncList(UIList list) {
        // 创建 Scene2D List
        List.ListStyle style = new List.ListStyle();
        style.font = new BitmapFont();
        style.fontColorSelected = GdxUIRenderer.getColor(list.getSelectedColor());
        style.fontColorUnselected = GdxUIRenderer.getColor(UIColor.WHITE);
        style.selection = createDrawable(list.getSelectedColor());
        style.background = createDrawable(list.getItemColor());

        List<String> gdxList = new List<>(style);

        // 设置列表项
        Array<String> items = new Array<>();
        for (UIListItem item : list.getItems()) {
            items.add(item.getText());
        }
        gdxList.setItems(items);

        // 设置选中项
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < items.size) {
            gdxList.setSelectedIndex(selectedIndex);
        }

        // 设置列表项点击监听
        gdxList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                list.setSelectedIndex(gdxList.getSelectedIndex());
            }
        });

        // 将 List 放入 ScrollPane
        ScrollPane scrollPane = new ScrollPane(gdxList);
        scrollPane.setSize(list.getWidth(), list.getHeight());

        // 根据布局方向设置滚动
        if (list.getLayoutDirection() == UIDirection.HORIZONTAL) {
            scrollPane.setScrollingDisabled(false, true);
        } else {
            scrollPane.setScrollingDisabled(true, false);
        }

        return scrollPane;
    }

    /**
     * 同步滑动条组件
     *
     * @param slider 核心 UI 滑动条组件
     * @return Scene2D Slider
     */
    public static Slider syncSlider(UISlider slider) {
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = createDrawable(slider.getBackgroundColor());
        style.knob = createDrawable(slider.getKnobColor());
        style.knobDown = createDrawable(slider.getKnobColor().lighten(0.1f));
        style.knobOver = createDrawable(slider.getKnobColor().lighten(0.15f));

        Slider gdxSlider = new Slider(
            slider.getMinValue(),
            slider.getMaxValue(),
            slider.getStepSize(),
            slider.isHorizontal(),
            style
        );
        gdxSlider.setSize(slider.getWidth(), slider.getHeight());
        gdxSlider.setValue(slider.getValue());
        gdxSlider.setDisabled(!slider.isEnabled() || !slider.isInteractable());

        // 监听值变化
        gdxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                slider.setValue(gdxSlider.getValue());
            }
        });

        return gdxSlider;
    }

    /**
     * 同步切换开关组件
     *
     * @param toggle 核心 UI 切换开关组件
     * @return Scene2D CheckBox (用作切换开关)
     */
    public static CheckBox syncToggle(UIToggle toggle) {
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(toggle.getTextColor());
        style.checkboxOn = createDrawable(toggle.getOnColor());
        style.checkboxOff = createDrawable(toggle.getOffColor());
        style.checkboxOnOver = createDrawable(toggle.getOnColor().lighten(0.1f));

        CheckBox checkBox = new CheckBox(toggle.getText(), style);
        checkBox.setSize(toggle.getWidth(), toggle.getHeight());
        checkBox.setChecked(toggle.isOn());
        checkBox.setDisabled(!toggle.isEnabled() || !toggle.isInteractable());

        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggle.setOn(checkBox.isChecked());
            }
        });

        return checkBox;
    }

    /**
     * 同步工具提示组件
     *
     * @param tooltip 核心 UI 工具提示组件
     * @return Scene2D Label (用作工具提示)
     */
    public static Label syncTooltip(UITooltip tooltip) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(tooltip.getTextColor());
        style.background = createDrawable(tooltip.getBackgroundColor());

        Label label = new Label(tooltip.getText(), style);
        label.setSize(tooltip.getWidth(), tooltip.getHeight());
        label.setVisible(tooltip.isVisible());

        return label;
    }

    /**
     * 同步加载指示器组件
     *
     * @param indicator 核心 UI 加载指示器组件
     * @return Scene2D Image (带动画)
     */
    public static Image syncLoadingIndicator(UILoadingIndicator indicator) {
        Image image = new Image(createDrawable(indicator.getColor()));
        image.setSize(indicator.getWidth(), indicator.getHeight());

        // 根据加载样式设置不同的 Actor
        // 实际动画由渲染层处理
        return image;
    }

    /**
     * 同步树形列表组件
     *
     * @param treeList 核心 UI 树形列表组件
     * @return Scene2D ScrollPane (包含 Tree List Table)
     */
    public static ScrollPane syncTreeList(UITreeList treeList) {
        // 创建 Table 来显示树形列表
        Table treeTable = new Table();

        // 获取所有可见节点
        for (UITreeNode node : treeList.getAllVisibleNodes()) {
            Label nodeLabel = new Label(
                node.getText(),
                new LabelStyle(new BitmapFont(), GdxUIRenderer.getColor(node.getTextColor()))
            );

            // 根据层级添加缩进
            int level = node.getLevel();
            treeTable.add(nodeLabel).left().padLeft(level * treeList.getIndentSize());
            treeTable.row();
        }

        // 放入 ScrollPane
        ScrollPane scrollPane = new ScrollPane(treeTable);
        scrollPane.setSize(treeList.getWidth(), treeList.getHeight());
        scrollPane.setScrollingDisabled(true, false);

        return scrollPane;
    }

    /**
     * 同步表格组件
     *
     * @param table 核心 UI 表格组件
     * @return Scene2D Table
     */
    public static Table syncTable(UITable table) {
        Table gdxTable = new Table();
        gdxTable.setSize(table.getWidth(), table.getHeight());

        // 创建表头
        if (table.isShowHeader()) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                UITableColumn column = table.getColumnAt(i);
                Label headerLabel = new Label(
                    column.getHeaderText() != null ? column.getHeaderText() : column.getName(),
                    new LabelStyle(new BitmapFont(), GdxUIRenderer.getColor(UIColor.WHITE))
                );
                gdxTable.add(headerLabel).width(column.getWidth()).pad(2);
            }
            gdxTable.row();
        }

        // 添加数据行
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                UITableCell cell = table.getCellAt(i, j);
                String value = "";
                if (cell != null && cell.getText() != null) {
                    value = cell.getText();
                }
                Label cellLabel = new Label(
                    value,
                    new LabelStyle(new BitmapFont(), GdxUIRenderer.getColor(UIColor.WHITE))
                );
                gdxTable.add(cellLabel).width(table.getColumnWidth(j)).pad(2);
            }
            gdxTable.row();
        }

        return gdxTable;
    }

    /**
     * 同步网格组件
     *
     * @param grid 核心 UI 网格组件
     * @return Scene2D Table
     */
    public static Table syncGrid(UIGrid grid) {
        Table gdxGrid = new Table();
        gdxGrid.setSize(grid.getWidth(), grid.getHeight());

        // 设置网格行列
        int rows = grid.getRows();
        int cols = grid.getColumns();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                UIGridCell cell = grid.getCellAt(i, j);
                String content = "";
                if (cell != null && cell.getContent() != null) {
                    content = cell.getContent().toString();
                }
                Label cellLabel = new Label(
                    content,
                    new LabelStyle(new BitmapFont(), GdxUIRenderer.getColor(UIColor.WHITE))
                );

                // 如果单元格被选中，设置背景色
                if (cell != null && cell.isSelected()) {
                    cellLabel.getStyle().background = createDrawable(grid.getSelectedColor());
                }

                gdxGrid.add(cellLabel).width(grid.getCellWidth()).height(grid.getCellHeight()).pad(1);
            }
            gdxGrid.row();
        }

        return gdxGrid;
    }

    /**
     * 同步分割面板组件
     *
     * @param splitPanel 核心 UI 分割面板组件
     * @return Scene2D Stack (包含两个组件和分割条)
     */
    public static Stack syncSplitPanel(UISplitPanel splitPanel) {
        Stack stack = new Stack();
        stack.setSize(splitPanel.getWidth(), splitPanel.getHeight());

        UIComponent firstComponent = splitPanel.getFirstComponent();
        UIComponent secondComponent = splitPanel.getSecondComponent();

        // 同步第一个组件
        Actor firstActor = null;
        if (firstComponent != null) {
            firstActor = syncComponent(firstComponent);
        }

        // 同步第二个组件
        Actor secondActor = null;
        if (secondComponent != null) {
            secondActor = syncComponent(secondComponent);
        }

        // 根据分割方向添加组件
        if (splitPanel.getOrientation() == UISplitPanel.SplitOrientation.VERTICAL) {
            // 垂直分割（左右）
            if (firstActor != null) {
                stack.add(firstActor);
            }
            if (secondActor != null) {
                stack.add(secondActor);
            }
        } else {
            // 水平分割（上下）
            if (firstActor != null) {
                stack.add(firstActor);
            }
            if (secondActor != null) {
                stack.add(secondActor);
            }
        }

        return stack;
    }

    /**
     * 同步模态对话框组件
     *
     * @param modalDialog 核心 UI 模态对话框组件
     * @return Scene2D Dialog
     */
    public static com.badlogic.gdx.scenes.scene2d.ui.Dialog syncModalDialog(UIModalDialog modalDialog) {
        com.badlogic.gdx.scenes.scene2d.ui.Dialog gdxDialog = new com.badlogic.gdx.scenes.scene2d.ui.Dialog(
            modalDialog.getTitle(),
            new WindowStyle(new BitmapFont(), GdxUIRenderer.getColor(UIColor.WHITE), null)
        );

        // 添加内容
        String message = modalDialog.getMessage();
        if (message != null && !message.isEmpty()) {
            Label contentLabel = new Label(
                message,
                new LabelStyle(new BitmapFont(), GdxUIRenderer.getColor(UIColor.WHITE))
            );
            gdxDialog.getContentTable().add(contentLabel).width(300);
            gdxDialog.getContentTable().row();
        }

        // 添加按钮
        for (UIModalDialog.ButtonInfo button : modalDialog.getButtons()) {
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
            style.font = new BitmapFont();
            style.fontColor = GdxUIRenderer.getColor(UIColor.WHITE);

            TextButton gdxButton = new TextButton(button.text, style);
            gdxDialog.button(gdxButton, button.result.ordinal());
        }

        gdxDialog.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // 对话框结果由 GdxUIRenderer 处理
            }
        });

        return gdxDialog;
    }

    /**
     * 同步组合下拉框
     *
     * @param comboBox 核心 UI 组合下拉框
     * @return Scene2D Actor
     */
    public static Actor syncComboBox(UIComboBox comboBox) {
        Table table = new Table();

        if (comboBox.isEditable()) {
            // 可编辑模式：使用 TextField
            TextFieldStyle style = new TextFieldStyle();
            style.font = new BitmapFont();
            style.fontColor = GdxUIRenderer.getColor(comboBox.getTextColor());
            TextField textField = new TextField(comboBox.getText(), style);
            table.add(textField).width(comboBox.getWidth());
        } else {
            // 只读模式：使用 Label 显示当前值
            LabelStyle style = new LabelStyle();
            style.font = new BitmapFont();
            style.fontColor = GdxUIRenderer.getColor(comboBox.getTextColor());
            Label label = new Label(comboBox.getDisplayText(), style);
            table.add(label).width(comboBox.getWidth());
        }

        table.setSize(comboBox.getWidth(), comboBox.getHeight());
        return table;
    }

    /**
     * 同步数字选择器
     *
     * @param picker 核心 UI 数字选择器
     * @return Scene2D Actor
     */
    public static Actor syncNumberPicker(UINumberPicker picker) {
        Table table = new Table();

        TextFieldStyle style = new TextFieldStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(picker.getTextColor());

        TextField textField = new TextField(picker.getDisplayText(), style);
        table.add(textField).width(100);

        if (picker.isShowButtons()) {
            TextButtonStyle btnStyle = new TextButtonStyle();
            btnStyle.font = new BitmapFont();

            TextButton minusBtn = new TextButton("-", btnStyle);
            TextButton plusBtn = new TextButton("+", btnStyle);

            minusBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    picker.stepDown();
                    textField.setText(picker.getDisplayText());
                }
            });

            plusBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    picker.stepUp();
                    textField.setText(picker.getDisplayText());
                }
            });

            table.add(minusBtn).width(40);
            table.add(plusBtn).width(40);
        }

        table.setSize(picker.getWidth(), picker.getHeight());
        return table;
    }

    /**
     * 同步颜色选择器
     *
     * @param picker 核心 UI 颜色选择器
     * @return Scene2D Actor
     */
    public static Actor syncColorPicker(UIColorPicker picker) {
        Table table = new Table();

        // 颜色预览
        Image colorPreview = new Image(createDrawable(picker.getSelectedColor()));
        table.add(colorPreview).size(50, 50);

        // RGB 滑块
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = createDrawable(new UIColor(0.3f, 0.3f, 0.3f, 1f));
        sliderStyle.knob = createDrawable(UIColor.WHITE);

        Slider redSlider = new Slider(0, 1, 0.01f, false, sliderStyle);
        redSlider.setValue(picker.getSelectedColor().getR());
        table.row();
        table.add(redSlider).width(150);

        Slider greenSlider = new Slider(0, 1, 0.01f, false, sliderStyle);
        greenSlider.setValue(picker.getSelectedColor().getG());
        table.row();
        table.add(greenSlider).width(150);

        Slider blueSlider = new Slider(0, 1, 0.01f, false, sliderStyle);
        blueSlider.setValue(picker.getSelectedColor().getB());
        table.row();
        table.add(blueSlider).width(150);

        table.setSize(picker.getWidth(), picker.getHeight());
        return table;
    }

    /**
     * 同步菜单栏
     *
     * @param menuBar 核心 UI 菜单栏
     * @return Scene2D Actor
     */
    public static Actor syncMenuBar(UIMenuBar menuBar) {
        Table table = new Table();
        table.defaults().pad(0);

        for (UIMenuItem item : menuBar.getItems()) {
            TextButtonStyle style = new TextButtonStyle();
            style.font = new BitmapFont();
            style.fontColor = GdxUIRenderer.getColor(menuBar.getTextColor());

            TextButton btn = new TextButton(item.getText(), style);
            table.add(btn).padRight(menuBar.getItemSpacing());
        }

        table.setSize(menuBar.getWidth(), menuBar.getItemHeight());
        return table;
    }

    /**
     * 同步上下文菜单
     *
     * @param menu 核心 UI 上下文菜单
     * @return Scene2D Actor
     */
    public static Actor syncContextMenu(UIContextMenu menu) {
        Table table = new Table();
        table.defaults().pad(2).fillX();

        for (UIMenuItem item : menu.getItems()) {
            if (item.isSeparator()) {
                Image sep = new Image(createDrawable(menu.getSeparatorColor()));
                table.add(sep).colspan(1).height(1).padLeft(20).padRight(5);
                table.row();
            } else {
                TextButtonStyle style = new TextButtonStyle();
                style.font = new BitmapFont();
                style.fontColor = GdxUIRenderer.getColor(menu.getTextColor());

                TextButton btn = new TextButton(item.getText(), style);
                table.add(btn);
                table.row();
            }
        }

        table.setPosition(menu.getX(), menu.getY());
        return table;
    }

    /**
     * 同步状态条
     *
     * @param statusBar 核心 UI 状态条
     * @return Scene2D Actor
     */
    public static Actor syncStatusBar(UIStatusBar statusBar) {
        Stack stack = new Stack();

        // 背景
        Image bg = new Image(createDrawable(statusBar.getBackgroundColor()));
        bg.setSize(statusBar.getWidth(), statusBar.getHeight());
        stack.add(bg);

        // 各层进度条
        Table layersTable = new Table();
        layersTable.setSize(statusBar.getWidth(), statusBar.getHeight());

        for (UIStatusBarLayer layer : statusBar.getLayers()) {
            if (layer.isVisible()) {
                Image layerImg = new Image(createDrawable(layer.getColor()));
                layerImg.setWidth(statusBar.getWidth() * layer.getProgressPercent());
                layerImg.setHeight(statusBar.getHeight());
                layersTable.add(layerImg);
            }
        }

        stack.add(layersTable);

        // 文本
        if (statusBar.isShowText()) {
            LabelStyle labelStyle = new LabelStyle();
            labelStyle.font = new BitmapFont();
            labelStyle.fontColor = GdxUIRenderer.getColor(statusBar.getTextColor());
            Label textLabel = new Label(statusBar.getComputedDisplayText(), labelStyle);
            stack.add(textLabel);
        }

        stack.setSize(statusBar.getWidth(), statusBar.getHeight());
        return stack;
    }

    /**
     * 同步虚拟摇杆
     *
     * @param joystick 核心 UI 虚拟摇杆
     * @return Scene2D Actor
     */
    public static Actor syncVirtualJoystick(UIVirtualJoystick joystick) {
        TouchpadStyle style = new TouchpadStyle();
        style.background = createDrawable(joystick.getStickColor());
        style.knob = createDrawable(joystick.getKnobColor());

        Touchpad touchpad = new Touchpad(joystick.getRadius(), style);
        touchpad.setSize(joystick.getTotalSize(), joystick.getTotalSize());

        touchpad.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                joystick.handleTouchStart(x, y);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                joystick.handleTouchDrag(x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                joystick.handleTouchEnd(x, y);
            }
        });

        return touchpad;
    }

    /**
     * 同步标签页容器组件
     *
     * @param tabContainer 核心 UI 标签页容器组件
     * @return Scene2D Stack (包含标签和内容)
     */
    public static Stack syncTabContainer(UITabContainer tabContainer) {
        Stack stack = new Stack();
        stack.setSize(tabContainer.getWidth(), tabContainer.getHeight());

        // 创建内容区域
        Table contentTable = new Table();
        contentTable.setSize(
            tabContainer.getWidth(),
            tabContainer.getHeight() - tabContainer.getTabHeight()
        );
        if (tabContainer.getContentColor() != null) {
            contentTable.setBackground(createDrawable(tabContainer.getContentColor()));
        }

        // 创建标签栏
        Table tabTable = new Table();
        tabTable.setHeight(tabContainer.getTabHeight());
        tabTable.setWidth(tabContainer.getWidth());

        // 添加标签
        for (int i = 0; i < tabContainer.getTabCount(); i++) {
            UITabContainer.Tab tab = tabContainer.getTabAt(i);
            TextButton tabButton = createTabButton(tab, tabContainer, i);
            tabTable.add(tabButton).padRight(tabContainer.getTabSpacing());
        }

        // 根据标签位置添加到 stack
        switch (tabContainer.getTabPosition()) {
            case TOP:
                stack.add(contentTable);
                stack.add(tabTable);
                break;
            case BOTTOM:
                stack.add(tabTable);
                stack.add(contentTable);
                break;
            default:
                stack.add(contentTable);
                break;
        }

        return stack;
    }

    /**
     * 创建标签按钮
     */
    private static TextButton createTabButton(UITabContainer.Tab tab, UITabContainer container, int index) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
        style.fontColor = GdxUIRenderer.getColor(container.getTextColor());
        style.up = createDrawable(container.getTabColor());
        style.down = createDrawable(container.getSelectedTabColor().darken(0.1f));
        style.over = createDrawable(container.getTabColor().lighten(0.1f));

        TextButton button = new TextButton(tab.getText(), style);
        button.setChecked(index == container.getSelectedTabIndex());
        button.setDisabled(!tab.isEnabled());

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                container.setSelectedTabIndex(index);
            }
        });

        return button;
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
        } else if (component instanceof UIImage) {
            return syncImage((UIImage) component);
        } else if (component instanceof UICheckbox) {
            return syncCheckbox((UICheckbox) component);
        } else if (component instanceof UIDropdown) {
            return syncDropdown((UIDropdown) component);
        } else if (component instanceof UIList) {
            return syncList((UIList) component);
        } else if (component instanceof UISlider) {
            return syncSlider((UISlider) component);
        } else if (component instanceof UIToggle) {
            return syncToggle((UIToggle) component);
        } else if (component instanceof UITooltip) {
            return syncTooltip((UITooltip) component);
        } else if (component instanceof UILoadingIndicator) {
            return syncLoadingIndicator((UILoadingIndicator) component);
        } else if (component instanceof UITabContainer) {
            return syncTabContainer((UITabContainer) component);
        } else if (component instanceof UITreeList) {
            return syncTreeList((UITreeList) component);
        } else if (component instanceof UITable) {
            return syncTable((UITable) component);
        } else if (component instanceof UIGrid) {
            return syncGrid((UIGrid) component);
        } else if (component instanceof UISplitPanel) {
            return syncSplitPanel((UISplitPanel) component);
        } else if (component instanceof UIModalDialog) {
            return syncModalDialog((UIModalDialog) component);
        } else if (component instanceof UIComboBox) {
            return syncComboBox((UIComboBox) component);
        } else if (component instanceof UINumberPicker) {
            return syncNumberPicker((UINumberPicker) component);
        } else if (component instanceof UIColorPicker) {
            return syncColorPicker((UIColorPicker) component);
        } else if (component instanceof UIMenuBar) {
            return syncMenuBar((UIMenuBar) component);
        } else if (component instanceof UIContextMenu) {
            return syncContextMenu((UIContextMenu) component);
        } else if (component instanceof UIStatusBar) {
            return syncStatusBar((UIStatusBar) component);
        } else if (component instanceof UIVirtualJoystick) {
            return syncVirtualJoystick((UIVirtualJoystick) component);
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
