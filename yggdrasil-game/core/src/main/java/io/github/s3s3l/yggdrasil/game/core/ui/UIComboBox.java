package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * UI 组合下拉框组件
 * 可编辑的下拉组合框，支持自定义输入和选项选择，支持自动完成/搜索过滤
 */
public class UIComboBox extends UIComponent {

    private List<String> options;                    // 选项列表
    private String text;                             // 当前文本（可编辑）
    private boolean editable;                        // 是否可编辑
    private boolean expanded;                        // 是否展开
    private boolean autoComplete;                    // 自动完成
    private UIColor textColor;                       // 文本颜色
    private UIColor dropdownColor;                   // 下拉框颜色
    private UIColor highlightColor;                  // 高亮颜色
    private float fontSize;                          // 字体大小
    private int maxVisibleItems;                     // 最大可见项数
    private String placeholder;                      // 占位符文本
    private ComboBoxListener listener;               // 选项变化监听器
    private List<String> filteredOptions;            // 过滤后的选项列表

    /**
     * 选项变化监听器接口
     */
    public interface ComboBoxListener {
        /**
         * 当选项改变时调用
         *
         * @param comboBox 组合框组件
         * @param value 选中的值
         */
        void onValueChanged(UIComboBox comboBox, String value);
    }

    public UIComboBox() {
        this(new ArrayList<>());
    }

    public UIComboBox(List<String> options) {
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.text = "";
        this.editable = true;
        this.expanded = false;
        this.autoComplete = false;
        this.textColor = UIColor.WHITE;
        this.dropdownColor = UIColor.GRAY;
        this.highlightColor = UIColor.BLUE;
        this.fontSize = 16;
        this.maxVisibleItems = 8;
        this.placeholder = "";
        this.filteredOptions = new ArrayList<>(this.options);
    }

    public List<String> getOptions() {
        return new ArrayList<>(options);
    }

    public void setOptions(List<String> options) {
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.filteredOptions = new ArrayList<>(this.options);
    }

    public void addOption(String option) {
        if (option != null && !options.contains(option)) {
            options.add(option);
            updateFilteredOptions();
        }
    }

    public void addOption(int index, String option) {
        if (option != null && !options.contains(option)) {
            options.add(index, option);
            updateFilteredOptions();
        }
    }

    public void removeOption(String option) {
        if (options.remove(option)) {
            updateFilteredOptions();
        }
    }

    public void removeOptionAt(int index) {
        if (index >= 0 && index < options.size()) {
            options.remove(index);
            updateFilteredOptions();
        }
    }

    public void clearOptions() {
        options.clear();
        filteredOptions.clear();
    }

    public int getOptionCount() {
        return options.size();
    }

    public String getOptionAt(int index) {
        if (index >= 0 && index < options.size()) {
            return options.get(index);
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        String oldValue = this.text;
        this.text = text != null ? text : "";
        if (autoComplete && !Objects.equals(oldValue, this.text)) {
            filterOptions(this.text);
        }
        if (listener != null && !Objects.equals(oldValue, this.text)) {
            listener.onValueChanged(this, this.text);
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * 展开下拉框
     */
    public void expand() {
        this.expanded = true;
        updateFilteredOptions();
    }

    /**
     * 收起下拉框
     */
    public void collapse() {
        this.expanded = false;
    }

    /**
     * 切换展开/收起状态
     */
    public void toggle() {
        this.expanded = !expanded;
        if (expanded) {
            updateFilteredOptions();
        }
    }

    public boolean isAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
        if (autoComplete) {
            filterOptions(text);
        }
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public UIColor getDropdownColor() {
        return dropdownColor;
    }

    public void setDropdownColor(UIColor dropdownColor) {
        this.dropdownColor = dropdownColor;
    }

    public UIColor getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(UIColor highlightColor) {
        this.highlightColor = highlightColor;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getMaxVisibleItems() {
        return maxVisibleItems;
    }

    public void setMaxVisibleItems(int maxVisibleItems) {
        this.maxVisibleItems = maxVisibleItems;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public ComboBoxListener getListener() {
        return listener;
    }

    public void setListener(ComboBoxListener listener) {
        this.listener = listener;
    }

    /**
     * 获取过滤后的选项列表
     *
     * @return 过滤后的选项列表
     */
    public List<String> getFilteredOptions() {
        return new ArrayList<>(filteredOptions);
    }

    /**
     * 过滤选项
     *
     * @param filter 过滤文本
     */
    public void filterOptions(String filter) {
        if (filter == null || filter.isEmpty()) {
            filteredOptions = new ArrayList<>(options);
        } else {
            String lowerFilter = filter.toLowerCase();
            filteredOptions = options.stream()
                    .filter(opt -> opt != null && opt.toLowerCase().contains(lowerFilter))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 更新过滤后的选项列表
     */
    private void updateFilteredOptions() {
        filterOptions(text);
    }

    /**
     * 选择指定选项
     *
     * @param option 选项文本
     */
    public void selectOption(String option) {
        if (option != null && options.contains(option)) {
            setText(option);
            collapse();
        }
    }

    /**
     * 选择指定索引的选项
     *
     * @param index 选项索引（基于过滤后的列表）
     */
    public void selectOptionAt(int index) {
        if (index >= 0 && index < filteredOptions.size()) {
            String option = filteredOptions.get(index);
            selectOption(option);
        }
    }

    /**
     * 处理选项点击
     *
     * @param optionIndex 选项索引（基于过滤后的列表）
     */
    public void handleOptionClick(int optionIndex) {
        if (optionIndex >= 0 && optionIndex < filteredOptions.size()) {
            selectOptionAt(optionIndex);
        }
    }

    /**
     * 处理输入
     *
     * @param input 输入的文本
     */
    public void handleInput(String input) {
        if (!editable || input == null) {
            return;
        }
        setText(text + input);
    }

    /**
     * 处理删除
     */
    public void handleBackspace() {
        if (!editable || text.isEmpty()) {
            return;
        }
        setText(text.substring(0, text.length() - 1));
    }

    @Override
    public void onClick() {
        if (!editable) {
            toggle();
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染组合框主体
        renderComboBox();

        // 如果展开，渲染下拉选项列表
        if (expanded) {
            renderDropdownItems();
        }
    }

    /**
     * 渲染组合框主体
     */
    protected void renderComboBox() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染下拉选项列表
     */
    protected void renderDropdownItems() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取显示文本（占位符或当前文本）
     *
     * @return 显示文本
     */
    public String getDisplayText() {
        if (text != null && !text.isEmpty()) {
            return text;
        }
        return placeholder;
    }

    /**
     * 检查是否有选项
     *
     * @return true 如果有选项
     */
    public boolean hasOptions() {
        return !options.isEmpty();
    }

    /**
     * 检查是否有过滤后的选项
     *
     * @return true 如果有过滤后的选项
     */
    public boolean hasFilteredOptions() {
        return !filteredOptions.isEmpty();
    }

    /**
     * 获取选中选项的索引
     *
     * @return 选中选项的索引，如果没有匹配则返回 -1
     */
    public int getSelectedIndex() {
        if (text == null || text.isEmpty()) {
            return -1;
        }
        return options.indexOf(text);
    }
}
