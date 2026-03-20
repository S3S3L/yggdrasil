package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 下拉框组件单元测试
 */
class UIDropdownTest {

    private UIDropdown dropdown;

    @BeforeEach
    void setUp() {
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3");
        dropdown = new UIDropdown(options);
        dropdown.setId(1L);
    }

    @Test
    void testDropdownCreation() {
        assertNotNull(dropdown);
        assertEquals(3, dropdown.getOptionCount());
        assertFalse(dropdown.isExpanded());
        assertFalse(dropdown.isAllowMultipleSelection());
        assertEquals("Select...", dropdown.getPlaceholder());
        assertEquals(-1, dropdown.getSelectedIndex());
    }

    @Test
    void testDefaultConstructor() {
        UIDropdown dd = new UIDropdown();
        assertNotNull(dd);
        assertEquals(0, dd.getOptionCount());
    }

    @Test
    void testConstructorWithNull() {
        UIDropdown dd = new UIDropdown(null);
        assertNotNull(dd);
        assertEquals(0, dd.getOptionCount());
    }

    @Test
    void testGetOptions() {
        List<String> options = dropdown.getOptions();
        assertEquals(3, options.size());
        assertEquals("Option 1", options.get(0));
    }

    @Test
    void testSetOptions() {
        List<String> newOptions = Arrays.asList("A", "B", "C", "D");
        dropdown.setOptions(newOptions);
        assertEquals(4, dropdown.getOptionCount());
        assertEquals("A", dropdown.getOptionAt(0));
    }

    @Test
    void testSetOptionsClearsSelection() {
        dropdown.setSelectedIndex(1);
        assertEquals(1, dropdown.getSelectedIndex());

        dropdown.setOptions(Arrays.asList("X", "Y"));
        assertEquals(-1, dropdown.getSelectedIndex());
    }

    @Test
    void testAddOption() {
        dropdown.addOption("Option 4");
        assertEquals(4, dropdown.getOptionCount());
        assertEquals("Option 4", dropdown.getOptionAt(3));
    }

    @Test
    void testAddOptionDuplicate() {
        dropdown.addOption("Option 1");
        assertEquals(3, dropdown.getOptionCount());
    }

    @Test
    void testAddOptionAt() {
        dropdown.addOption(1, "New Option");
        assertEquals(4, dropdown.getOptionCount());
        assertEquals("New Option", dropdown.getOptionAt(1));
    }

    @Test
    void testRemoveOption() {
        dropdown.removeOption("Option 2");
        assertEquals(2, dropdown.getOptionCount());
        assertNull(dropdown.getOptionAt(2));
    }

    @Test
    void testRemoveOptionNotFound() {
        dropdown.removeOption("Non Existent");
        assertEquals(3, dropdown.getOptionCount());
    }

    @Test
    void testRemoveOptionAt() {
        dropdown.removeOptionAt(1);
        assertEquals(2, dropdown.getOptionCount());
        assertEquals("Option 3", dropdown.getOptionAt(1));
    }

    @Test
    void testClearOptions() {
        dropdown.clearOptions();
        assertEquals(0, dropdown.getOptionCount());
        assertEquals(-1, dropdown.getSelectedIndex());
    }

    @Test
    void testGetOptionAt() {
        assertEquals("Option 1", dropdown.getOptionAt(0));
        assertEquals("Option 2", dropdown.getOptionAt(1));
        assertEquals("Option 3", dropdown.getOptionAt(2));
        assertNull(dropdown.getOptionAt(-1));
        assertNull(dropdown.getOptionAt(10));
    }

    @Test
    void testSetExpanded() {
        dropdown.setExpanded(true);
        assertTrue(dropdown.isExpanded());

        dropdown.setExpanded(false);
        assertFalse(dropdown.isExpanded());
    }

    @Test
    void testExpand() {
        dropdown.expand();
        assertTrue(dropdown.isExpanded());
    }

    @Test
    void testCollapse() {
        dropdown.expand();
        dropdown.collapse();
        assertFalse(dropdown.isExpanded());
    }

    @Test
    void testToggle() {
        assertFalse(dropdown.isExpanded());

        dropdown.toggle();
        assertTrue(dropdown.isExpanded());

        dropdown.toggle();
        assertFalse(dropdown.isExpanded());
    }

    @Test
    void testSetAllowMultipleSelection() {
        dropdown.setAllowMultipleSelection(true);
        assertTrue(dropdown.isAllowMultipleSelection());

        dropdown.setAllowMultipleSelection(false);
        assertFalse(dropdown.isAllowMultipleSelection());
    }

    @Test
    void testSetPlaceholder() {
        dropdown.setPlaceholder("Choose...");
        assertEquals("Choose...", dropdown.getPlaceholder());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.YELLOW;
        dropdown.setTextColor(color);
        assertEquals(color, dropdown.getTextColor());
    }

    @Test
    void testSetDropdownColor() {
        UIColor color = UIColor.BLUE;
        dropdown.setDropdownColor(color);
        assertEquals(color, dropdown.getDropdownColor());
    }

    @Test
    void testSetFontSize() {
        dropdown.setFontSize(20);
        assertEquals(20, dropdown.getFontSize());
    }

    @Test
    void testSetMaxVisibleItems() {
        dropdown.setMaxVisibleItems(10);
        assertEquals(10, dropdown.getMaxVisibleItems());
    }

    @Test
    void testSetSelectedIndex() {
        dropdown.setSelectedIndex(1);
        assertEquals(1, dropdown.getSelectedIndex());
        assertEquals("Option 2", dropdown.getSelectedOption());
    }

    @Test
    void testSetSelectedIndexOutOfBounds() {
        dropdown.setSelectedIndex(-1);
        assertEquals(-1, dropdown.getSelectedIndex());

        dropdown.setSelectedIndex(10);
        assertEquals(-1, dropdown.getSelectedIndex());
    }

    @Test
    void testSetSelectedOption() {
        dropdown.setSelectedOption("Option 2");
        assertEquals("Option 2", dropdown.getSelectedOption());
        assertEquals(1, dropdown.getSelectedIndex());
    }

    @Test
    void testSetSelectedOptionNotFound() {
        dropdown.setSelectedOption("Non Existent");
        assertEquals(-1, dropdown.getSelectedIndex());
    }

    @Test
    void testGetSelectedIndices() {
        dropdown.setAllowMultipleSelection(true);
        dropdown.setSelectedIndex(0);
        dropdown.setSelectedIndex(2);

        List<Integer> indices = dropdown.getSelectedIndices();
        assertEquals(2, indices.size());
        assertTrue(indices.contains(0));
        assertTrue(indices.contains(2));
    }

    @Test
    void testGetSelectedOptions() {
        dropdown.setAllowMultipleSelection(true);
        dropdown.setSelectedIndex(0);
        dropdown.setSelectedIndex(1);

        List<String> options = dropdown.getSelectedOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains("Option 1"));
        assertTrue(options.contains("Option 2"));
    }

    @Test
    void testIsSelected() {
        dropdown.setSelectedIndex(1);
        assertTrue(dropdown.isSelected(1));
        assertFalse(dropdown.isSelected(0));
    }

    @Test
    void testClearSelection() {
        dropdown.setSelectedIndex(1);
        dropdown.clearSelection();
        assertEquals(-1, dropdown.getSelectedIndex());
    }

    @Test
    void testSelectionListener() {
        final boolean[] listenerCalled = {false};
        final String[] capturedOption = {null};

        dropdown.setListener(new UIDropdown.SelectionListener() {
            @Override
            public void onSelectionChanged(UIDropdown dropdown, String selectedOption, int selectedIndex) {
                listenerCalled[0] = true;
                capturedOption[0] = selectedOption;
            }

            @Override
            public void onSelectionChanged(UIDropdown dropdown, List<String> selectedOptions, List<Integer> selectedIndices) {
                // 多选模式
            }
        });

        dropdown.setSelectedIndex(1);

        assertTrue(listenerCalled[0]);
        assertEquals("Option 2", capturedOption[0]);
    }

    @Test
    void testOnClick() {
        assertFalse(dropdown.isExpanded());
        dropdown.onClick();
        assertTrue(dropdown.isExpanded());
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> dropdown.render());
    }

    @Test
    void testRenderHidden() {
        dropdown.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> dropdown.render());
    }

    @Test
    void testHandleOptionClick() {
        dropdown.handleOptionClick(1);
        assertEquals(1, dropdown.getSelectedIndex());
        assertFalse(dropdown.isExpanded());
    }

    @Test
    void testListenerNullByDefault() {
        assertNull(dropdown.getListener());
    }
}
