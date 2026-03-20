package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 组合下拉框组件单元测试
 */
class UIComboBoxTest {

    private UIComboBox comboBox;

    @BeforeEach
    void setUp() {
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3");
        comboBox = new UIComboBox(options);
        comboBox.setId(1L);
    }

    @Test
    void testComboBoxCreation() {
        assertNotNull(comboBox);
        assertEquals(3, comboBox.getOptionCount());
        assertFalse(comboBox.isExpanded());
        assertTrue(comboBox.isEditable());
        assertFalse(comboBox.isAutoComplete());
    }

    @Test
    void testDefaultConstructor() {
        UIComboBox cb = new UIComboBox();
        assertNotNull(cb);
        assertEquals(0, cb.getOptionCount());
    }

    @Test
    void testNullOptions() {
        UIComboBox cb = new UIComboBox(null);
        assertNotNull(cb);
        assertEquals(0, cb.getOptionCount());
    }

    @Test
    void testAddOption() {
        comboBox.addOption("Option 4");
        assertEquals(4, comboBox.getOptionCount());
        assertTrue(comboBox.getOptions().contains("Option 4"));
    }

    @Test
    void testAddOptionAt() {
        comboBox.addOption(0, "First Option");
        assertEquals(4, comboBox.getOptionCount());
        assertEquals("First Option", comboBox.getOptionAt(0));
    }

    @Test
    void testAddDuplicateOption() {
        comboBox.addOption("Option 1");
        assertEquals(3, comboBox.getOptionCount()); // 不添加重复项
    }

    @Test
    void testRemoveOption() {
        comboBox.removeOption("Option 2");
        assertEquals(2, comboBox.getOptionCount());
        assertFalse(comboBox.getOptions().contains("Option 2"));
    }

    @Test
    void testRemoveOptionAt() {
        comboBox.removeOptionAt(1);
        assertEquals(2, comboBox.getOptionCount());
    }

    @Test
    void testClearOptions() {
        comboBox.clearOptions();
        assertEquals(0, comboBox.getOptionCount());
    }

    @Test
    void testGetOptionAt() {
        assertEquals("Option 1", comboBox.getOptionAt(0));
        assertNull(comboBox.getOptionAt(-1));
        assertNull(comboBox.getOptionAt(10));
    }

    @Test
    void testSetText() {
        comboBox.setText("New Text");
        assertEquals("New Text", comboBox.getText());
    }

    @Test
    void testSetNullText() {
        comboBox.setText(null);
        assertEquals("", comboBox.getText());
    }

    @Test
    void testSetEditable() {
        comboBox.setEditable(false);
        assertFalse(comboBox.isEditable());
    }

    @Test
    void testExpand() {
        comboBox.expand();
        assertTrue(comboBox.isExpanded());
    }

    @Test
    void testCollapse() {
        comboBox.expand();
        comboBox.collapse();
        assertFalse(comboBox.isExpanded());
    }

    @Test
    void testToggle() {
        comboBox.toggle();
        assertTrue(comboBox.isExpanded());
        comboBox.toggle();
        assertFalse(comboBox.isExpanded());
    }

    @Test
    void testSetAutoComplete() {
        comboBox.setText("Option");
        comboBox.setAutoComplete(true);
        assertTrue(comboBox.isAutoComplete());
        assertEquals(3, comboBox.getFilteredOptions().size()); // 所有选项都包含"Option"
    }

    @Test
    void testFilterOptions() {
        comboBox.filterOptions("Option 1");
        assertEquals(1, comboBox.getFilteredOptions().size());
        assertEquals("Option 1", comboBox.getFilteredOptions().get(0));
    }

    @Test
    void testFilterOptionsEmpty() {
        comboBox.filterOptions("");
        assertEquals(3, comboBox.getFilteredOptions().size());
    }

    @Test
    void testFilterOptionsNoMatch() {
        comboBox.filterOptions("NonExistent");
        assertEquals(0, comboBox.getFilteredOptions().size());
    }

    @Test
    void testSelectOption() {
        comboBox.selectOption("Option 2");
        assertEquals("Option 2", comboBox.getText());
        assertFalse(comboBox.isExpanded());
    }

    @Test
    void testSelectOptionNotInList() {
        comboBox.selectOption("NonExistent");
        assertEquals("", comboBox.getText());
    }

    @Test
    void testSelectOptionAt() {
        comboBox.expand();
        comboBox.selectOptionAt(1);
        assertEquals("Option 2", comboBox.getText());
    }

    @Test
    void testHandleOptionClick() {
        comboBox.expand();
        comboBox.handleOptionClick(0);
        assertEquals("Option 1", comboBox.getText());
    }

    @Test
    void testHandleInput() {
        comboBox.setEditable(true);
        comboBox.setText("Opt");
        comboBox.handleInput("ion");
        assertEquals("Option", comboBox.getText());
    }

    @Test
    void testHandleInputNotEditable() {
        comboBox.setEditable(false);
        comboBox.setText("Opt");
        comboBox.handleInput("ion");
        assertEquals("Opt", comboBox.getText());
    }

    @Test
    void testHandleBackspace() {
        comboBox.setText("Test");
        comboBox.handleBackspace();
        assertEquals("Tes", comboBox.getText());
    }

    @Test
    void testHandleBackspaceEmpty() {
        comboBox.setText("");
        comboBox.handleBackspace();
        assertEquals("", comboBox.getText());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.RED;
        comboBox.setTextColor(color);
        assertEquals(color, comboBox.getTextColor());
    }

    @Test
    void testSetDropdownColor() {
        UIColor color = UIColor.BLUE;
        comboBox.setDropdownColor(color);
        assertEquals(color, comboBox.getDropdownColor());
    }

    @Test
    void testSetHighlightColor() {
        UIColor color = UIColor.YELLOW;
        comboBox.setHighlightColor(color);
        assertEquals(color, comboBox.getHighlightColor());
    }

    @Test
    void testSetFontSize() {
        comboBox.setFontSize(20);
        assertEquals(20, comboBox.getFontSize());
    }

    @Test
    void testSetMaxVisibleItems() {
        comboBox.setMaxVisibleItems(10);
        assertEquals(10, comboBox.getMaxVisibleItems());
    }

    @Test
    void testSetPlaceholder() {
        comboBox.setPlaceholder("Select an option");
        assertEquals("Select an option", comboBox.getPlaceholder());
    }

    @Test
    void testListener() {
        final String[] capturedValue = {null};
        comboBox.setListener((cb, value) -> capturedValue[0] = value);

        comboBox.setText("Test");
        assertEquals("Test", capturedValue[0]);
    }

    @Test
    void testGetDisplayText() {
        comboBox.setText("Selected");
        assertEquals("Selected", comboBox.getDisplayText());
    }

    @Test
    void testGetDisplayTextWithPlaceholder() {
        comboBox.setText("");
        comboBox.setPlaceholder("Choose...");
        assertEquals("Choose...", comboBox.getDisplayText());
    }

    @Test
    void testHasOptions() {
        assertTrue(comboBox.hasOptions());
        comboBox.clearOptions();
        assertFalse(comboBox.hasOptions());
    }

    @Test
    void testHasFilteredOptions() {
        assertTrue(comboBox.hasFilteredOptions());
        comboBox.filterOptions("NonExistent");
        assertFalse(comboBox.hasFilteredOptions());
    }

    @Test
    void testGetSelectedIndex() {
        comboBox.setText("Option 2");
        assertEquals(1, comboBox.getSelectedIndex());
    }

    @Test
    void testGetSelectedIndexNotFound() {
        comboBox.setText("NonExistent");
        assertEquals(-1, comboBox.getSelectedIndex());
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> comboBox.render());
    }

    @Test
    void testRenderHidden() {
        comboBox.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> comboBox.render());
    }

    @Test
    void testOnClick() {
        comboBox.setEditable(false);
        comboBox.onClick();
        assertTrue(comboBox.isExpanded());
    }

    @Test
    void testOnClickEditable() {
        comboBox.setEditable(true);
        comboBox.onClick();
        // 可编辑模式下点击不切换展开状态
        assertFalse(comboBox.isExpanded());
    }
}
