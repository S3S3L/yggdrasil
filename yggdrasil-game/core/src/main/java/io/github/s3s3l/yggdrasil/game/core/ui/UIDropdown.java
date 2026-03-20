package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * UI 下拉框组件
 * 可展开/收起的选项列表，支持单选/多选
 */
public class UIDropdown extends UIComponent {

    private List<String> options;                    // 选项列表
    private List<Integer> selectedIndices;           // 选中项索引（支持多选）
    private boolean expanded;                        // 是否展开
    private boolean allowMultipleSelection;          // 是否允许多选
    private String placeholder;                      // 占位符文本
    private UIColor textColor;                       // 文本颜色
    private UIColor dropdownColor;                   // 下拉框颜色
    private float fontSize;                          // 字体大小
    private int maxVisibleItems;                     // 最大可见项数
    private SelectionListener listener;              // 选项变化监听器

    /**
     * 选项变化监听器接口
     */
    public interface SelectionListener {
        /**
         * 当选项改变时调用（单选模式）
         *
         * @param dropdown 下拉框组件
         * @param selectedOption 选中的选项
         * @param selectedIndex 选中的索引
         */
        void onSelectionChanged(UIDropdown dropdown, String selectedOption, int selectedIndex);

        /**
         * 当选项改变时调用（多选模式）
         *
         * @param dropdown 下拉框组件
         * @param selectedOptions 选中的选项列表
         * @param selectedIndices 选中的索引列表
         */
        void onSelectionChanged(UIDropdown dropdown, List<String> selectedOptions, List<Integer> selectedIndices);
    }

    public UIDropdown() {
        this(new ArrayList<>());
    }

    public UIDropdown(List<String> options) {
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.selectedIndices = new ArrayList<>();
        this.expanded = false;
        this.allowMultipleSelection = false;
        this.placeholder = "Select...";
        this.textColor = UIColor.WHITE;
        this.dropdownColor = UIColor.GRAY;
        this.fontSize = 16;
        this.maxVisibleItems = 8;
    }

    public List<String> getOptions() {
        return new ArrayList<>(options);
    }

    public void setOptions(List<String> options) {
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.selectedIndices.clear();
    }

    public void addOption(String option) {
        if (option != null && !options.contains(option)) {
            options.add(option);
        }
    }

    public void addOption(int index, String option) {
        if (option != null && !options.contains(option)) {
            options.add(index, option);
        }
    }

    public void removeOption(String option) {
        int index = options.indexOf(option);
        if (index >= 0) {
            options.remove(index);
            // 调整选中索引
            adjustSelectedIndicesAfterRemove(index);
        }
    }

    public void removeOptionAt(int index) {
        if (index >= 0 && index < options.size()) {
            options.remove(index);
            adjustSelectedIndicesAfterRemove(index);
        }
    }

    public void clearOptions() {
        options.clear();
        selectedIndices.clear();
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
    }

    public boolean isAllowMultipleSelection() {
        return allowMultipleSelection;
    }

    public void setAllowMultipleSelection(boolean allowMultipleSelection) {
        this.allowMultipleSelection = allowMultipleSelection;
        if (!allowMultipleSelection && selectedIndices.size() > 1) {
            // 如果从多选切换到单选，只保留第一个选中项
            while (selectedIndices.size() > 1) {
                selectedIndices.remove(selectedIndices.size() - 1);
            }
        }
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
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

    public SelectionListener getListener() {
        return listener;
    }

    public void setListener(SelectionListener listener) {
        this.listener = listener;
    }

    /**
     * 获取选中的索引（单选模式）
     *
     * @return 选中的索引，如果没有选中项则返回 -1
     */
    public int getSelectedIndex() {
        if (selectedIndices.isEmpty()) {
            return -1;
        }
        return selectedIndices.get(0);
    }

    /**
     * 设置选中的索引（单选模式）
     *
     * @param index 选中的索引
     */
    public void setSelectedIndex(int index) {
        if (allowMultipleSelection) {
            // 多选模式下，切换选中状态
            if (index >= 0 && index < options.size()) {
                if (selectedIndices.contains(index)) {
                    selectedIndices.remove(Integer.valueOf(index));
                } else {
                    selectedIndices.add(index);
                }
                notifyListenerMulti();
            }
        } else {
            // 单选模式
            if (index >= 0 && index < options.size()) {
                selectedIndices.clear();
                selectedIndices.add(index);
                notifyListenerSingle();
            }
        }
    }

    /**
     * 获取选中的选项（单选模式）
     *
     * @return 选中的选项，如果没有选中项则返回 null
     */
    public String getSelectedOption() {
        int index = getSelectedIndex();
        if (index >= 0 && index < options.size()) {
            return options.get(index);
        }
        return null;
    }

    /**
     * 设置选中的选项（单选模式）
     *
     * @param option 选中的选项
     */
    public void setSelectedOption(String option) {
        int index = options.indexOf(option);
        setSelectedIndex(index);
    }

    /**
     * 获取所有选中的索引（多选模式）
     *
     * @return 选中的索引列表
     */
    public List<Integer> getSelectedIndices() {
        return new ArrayList<>(selectedIndices);
    }

    /**
     * 获取所有选中的选项（多选模式）
     *
     * @return 选中的选项列表
     */
    public List<String> getSelectedOptions() {
        List<String> result = new ArrayList<>();
        for (int index : selectedIndices) {
            if (index >= 0 && index < options.size()) {
                result.add(options.get(index));
            }
        }
        return result;
    }

    /**
     * 检查指定索引是否被选中
     *
     * @param index 索引
     * @return 是否选中
     */
    public boolean isSelected(int index) {
        return selectedIndices.contains(index);
    }

    /**
     * 清空选中项
     */
    public void clearSelection() {
        selectedIndices.clear();
        if (listener != null) {
            if (allowMultipleSelection) {
                listener.onSelectionChanged(this, new ArrayList<>(), new ArrayList<>());
            } else {
                listener.onSelectionChanged(this, null, -1);
            }
        }
    }

    @Override
    public void onClick() {
        toggle();
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染下拉框主体
        renderDropdown();

        // 如果展开，渲染下拉选项列表
        if (expanded) {
            renderDropdownItems();
        }
    }

    /**
     * 渲染下拉框主体
     */
    protected void renderDropdown() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染下拉选项列表
     */
    protected void renderDropdownItems() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 处理选项点击
     *
     * @param optionIndex 选项索引
     */
    public void handleOptionClick(int optionIndex) {
        if (optionIndex >= 0 && optionIndex < options.size()) {
            setSelectedIndex(optionIndex);
            if (!allowMultipleSelection) {
                collapse();
            }
        }
    }

    /**
     * 通知监听器（单选模式）
     */
    private void notifyListenerSingle() {
        if (listener != null) {
            int index = getSelectedIndex();
            String option = index >= 0 ? options.get(index) : null;
            listener.onSelectionChanged(this, option, index);
        }
    }

    /**
     * 通知监听器（多选模式）
     */
    private void notifyListenerMulti() {
        if (listener != null) {
            listener.onSelectionChanged(this, getSelectedOptions(), getSelectedIndices());
        }
    }

    /**
     * 移除选项后调整选中索引
     */
    private void adjustSelectedIndicesAfterRemove(int removedIndex) {
        // 从后向前遍历，避免索引变化问题
        for (int i = selectedIndices.size() - 1; i >= 0; i--) {
            int selectedIndex = selectedIndices.get(i);
            if (selectedIndex == removedIndex) {
                // 移除被删除的选中项
                selectedIndices.remove(i);
            } else if (selectedIndex > removedIndex) {
                // 调整索引
                selectedIndices.set(i, selectedIndex - 1);
            }
        }
    }
}
