package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 网格布局组件单元测试
 */
class UIGridTest {

    private UIGrid grid;

    @BeforeEach
    void setUp() {
        grid = new UIGrid(3, 3);
        grid.setId(1L);
        grid.setSize(300, 300);
    }

    @Test
    void testGridCreation() {
        assertNotNull(grid);
        assertEquals(3, grid.getColumns());
        assertEquals(3, grid.getRows());
        assertEquals(9, grid.getCellCount());
        assertEquals(UIGrid.SelectionMode.SINGLE, grid.getSelectionMode());
        assertTrue(grid.isShowGridLines());
    }

    @Test
    void testConstructor() {
        UIGrid g = new UIGrid();
        assertNotNull(g);
        assertEquals(3, g.getColumns());
        assertEquals(3, g.getRows());
    }

    @Test
    void testSetColumns() {
        grid.setColumns(4);
        assertEquals(4, grid.getColumns());
        assertEquals(12, grid.getCellCount()); // 4 x 3
    }

    @Test
    void testSetRows() {
        grid.setRows(4);
        assertEquals(4, grid.getRows());
        assertEquals(12, grid.getCellCount()); // 3 x 4
    }

    @Test
    void testSetCellWidth() {
        grid.setCellWidth(150);
        assertEquals(150, grid.getCellWidth(), 0.01f);
    }

    @Test
    void testSetCellHeight() {
        grid.setCellHeight(150);
        assertEquals(150, grid.getCellHeight(), 0.01f);
    }

    @Test
    void testSetSpacing() {
        grid.setSpacing(5);
        assertEquals(5, grid.getSpacing(), 0.01f);
    }

    @Test
    void testSetCellColor() {
        UIColor color = UIColor.BLUE;
        grid.setCellColor(color);
        assertEquals(color, grid.getCellColor());
    }

    @Test
    void testSetSelectedColor() {
        UIColor color = UIColor.GREEN;
        grid.setSelectedColor(color);
        assertEquals(color, grid.getSelectedColor());
    }

    @Test
    void testSetHoverColor() {
        UIColor color = UIColor.GRAY;
        grid.setHoverColor(color);
        assertEquals(color, grid.getHoverColor());
    }

    @Test
    void testSetGridColor() {
        UIColor color = UIColor.YELLOW;
        grid.setGridColor(color);
        assertEquals(color, grid.getGridColor());
    }

    @Test
    void testSetShowGridLines() {
        grid.setShowGridLines(false);
        assertFalse(grid.isShowGridLines());
    }

    @Test
    void testSetSelectionMode() {
        grid.setSelectionMode(UIGrid.SelectionMode.NONE);
        assertEquals(UIGrid.SelectionMode.NONE, grid.getSelectionMode());
    }

    @Test
    void testGetCellAt() {
        UIGridCell cell = grid.getCellAt(1, 1);
        assertNotNull(cell);
        assertEquals(1, cell.getRow());
        assertEquals(1, cell.getColumn());

        assertNull(grid.getCellAt(10, 10));
    }

    @Test
    void testSetCellAt() {
        UIGridCell newCell = new UIGridCell(0, 0);
        newCell.setContent("Test");
        grid.setCellAt(0, 0, newCell);

        assertSame(newCell, grid.getCellAt(0, 0));
        assertEquals("Test", grid.getCellAt(0, 0).getContent());
    }

    @Test
    void testGetCellContent() {
        grid.setCellContent(1, 1, "Test Content");
        assertEquals("Test Content", grid.getCellContent(1, 1));
    }

    @Test
    void testSetCellContent() {
        grid.setCellContent(0, 0, "New Content");
        assertEquals("New Content", grid.getCellContent(0, 0));
    }

    @Test
    void testSelectCell() {
        UIGridCell cell = grid.getCellAt(1, 1);
        assertFalse(cell.isSelected());

        grid.selectCell(cell);

        assertTrue(cell.isSelected());
        assertSame(cell, grid.getSelectedCell());
    }

    @Test
    void testSelectCellByPosition() {
        grid.selectCell(1, 1);

        UIGridCell cell = grid.getSelectedCell();
        assertNotNull(cell);
        assertEquals(1, cell.getRow());
        assertEquals(1, cell.getColumn());
    }

    @Test
    void testDeselectAll() {
        grid.selectCell(0, 0);
        grid.selectCell(1, 1); // 单选模式下会取消第一个的选择

        grid.deselectAll();

        assertNull(grid.getSelectedCell());
        assertFalse(grid.getCellAt(0, 0).isSelected());
    }

    @Test
    void testGetSelectedCells() {
        grid.setSelectionMode(UIGrid.SelectionMode.MULTI_CELL);
        grid.selectCell(0, 0);
        grid.selectCell(1, 1);

        assertEquals(2, grid.getSelectedCells().size());
    }

    @Test
    void testGetCellBounds() {
        float[] bounds = grid.getCellBounds(0, 0);
        assertNotNull(bounds);
        assertEquals(4, bounds.length);
        assertTrue(bounds[2] > 0); // width
        assertTrue(bounds[3] > 0); // height
    }

    @Test
    void testGetCellAtPosition() {
        float[] bounds = grid.getCellBounds(0, 0);
        UIGridCell cell = grid.getCellAtPosition(bounds[0] + 10, bounds[1] + 10);
        assertNotNull(cell);
    }

    @Test
    void testClearCells() {
        grid.setCellContent(0, 0, "Test");
        grid.selectCell(0, 0);

        grid.clearCells();

        assertNull(grid.getCellAt(0, 0).getContent());
        assertFalse(grid.getCellAt(0, 0).isSelected());
        assertNull(grid.getSelectedCell());
    }

    @Test
    void testListener() {
        final UIGridCell[] selectedCell = {null};
        final int[] clickedRow = {-1};
        final int[] clickedCol = {-1};
        final int[] doubleClickedRow = {-1};
        final int[] doubleClickedCol = {-1};

        grid.setListener(new UIGrid.GridListener() {
            @Override
            public void onCellSelected(UIGrid grid, UIGridCell cell) {
                selectedCell[0] = cell;
            }

            @Override
            public void onCellClicked(UIGrid grid, int row, int column) {
                clickedRow[0] = row;
                clickedCol[0] = column;
            }

            @Override
            public void onCellDoubleClicked(UIGrid grid, int row, int column) {
                doubleClickedRow[0] = row;
                doubleClickedCol[0] = column;
            }
        });

        UIGridCell cell = grid.getCellAt(1, 1);
        grid.selectCell(cell);
        assertSame(cell, selectedCell[0]);
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> grid.render());
    }

    @Test
    void testRenderHidden() {
        grid.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> grid.render());
    }

    @Test
    void testGetPreferredWidth() {
        grid.setCellWidth(100);
        grid.setSpacing(2);

        float width = grid.getPreferredWidth();
        // 3 列 * 100 + 4 个间距 * 2
        assertEquals(308, width, 0.01f);
    }

    @Test
    void testGetPreferredHeight() {
        grid.setCellHeight(100);
        grid.setSpacing(2);

        float height = grid.getPreferredHeight();
        // 3 行 * 100 + 4 个间距 * 2
        assertEquals(308, height, 0.01f);
    }

    @Test
    void testSetCells() {
        java.util.List<UIGridCell> cells = new java.util.ArrayList<>();
        for (int i = 0; i < 9; i++) {
            cells.add(new UIGridCell(i / 3, i % 3));
        }
        grid.setCells(cells);

        assertEquals(9, grid.getCells().size());
    }

    @Test
    void testSelectionModeNone() {
        grid.setSelectionMode(UIGrid.SelectionMode.NONE);
        UIGridCell cell = grid.getCellAt(0, 0);
        grid.selectCell(cell);

        // 在 NONE 模式下不应该被选中
        assertFalse(cell.isSelected());
    }

    @Test
    void testGetCellAtOutOfBounds() {
        assertNull(grid.getCellAt(10, 10));
        assertNull(grid.getCellAt(-1, 0));
    }

    @Test
    void testSetCellAtOutOfBounds() {
        grid.setCellAt(10, 10, new UIGridCell()); // 应该不抛异常
        assertEquals(9, grid.getCellCount());
    }
}
