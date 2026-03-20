package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 表格组件单元测试
 */
class UITableTest {

    private UITable table;

    @BeforeEach
    void setUp() {
        table = new UITable();
        table.setId(1L);
        table.setSize(400, 300);
    }

    @Test
    void testTableCreation() {
        assertNotNull(table);
        assertEquals(0, table.getColumnCount());
        assertEquals(0, table.getRowCount());
        assertEquals(-1, table.getSelectedRow());
        assertEquals(-1, table.getSelectedColumn());
        assertTrue(table.isShowHeader());
        assertTrue(table.isShowGridLines());
    }

    @Test
    void testAddColumn() {
        UITableColumn column = new UITableColumn("Column 1");
        table.addColumn(column);

        assertEquals(1, table.getColumnCount());
        assertSame(column, table.getColumnAt(0));
    }

    @Test
    void testAddColumnAtIndex() {
        table.addColumn(new UITableColumn("Column 1"));
        table.addColumn(new UITableColumn("Column 2"));
        table.addColumn(0, new UITableColumn("New Column"));

        assertEquals(3, table.getColumnCount());
        assertEquals("New Column", table.getColumnAt(0).getName());
    }

    @Test
    void testRemoveColumn() {
        UITableColumn column1 = new UITableColumn("Column 1");
        UITableColumn column2 = new UITableColumn("Column 2");
        table.addColumn(column1);
        table.addColumn(column2);

        table.removeColumn(column1);

        assertEquals(1, table.getColumnCount());
        assertSame(column2, table.getColumnAt(0));
    }

    @Test
    void testRemoveColumnAt() {
        table.addColumn(new UITableColumn("Column 1"));
        table.addColumn(new UITableColumn("Column 2"));
        table.addColumn(new UITableColumn("Column 3"));

        table.removeColumnAt(1);

        assertEquals(2, table.getColumnCount());
        assertEquals("Column 3", table.getColumnAt(1).getName());
    }

    @Test
    void testFindColumnByName() {
        table.addColumn(new UITableColumn("Column 1"));
        table.addColumn(new UITableColumn("Column 2"));
        table.addColumn(new UITableColumn("Column 3"));

        assertEquals(1, table.findColumnByName("Column 2"));
        assertEquals(-1, table.findColumnByName("Non Existent"));
    }

    @Test
    void testAddRow() {
        List<UITableCell> cells = Arrays.asList(
            new UITableCell("Cell 1"),
            new UITableCell("Cell 2"),
            new UITableCell("Cell 3")
        );
        table.addRow(cells);

        assertEquals(1, table.getRowCount());
        assertEquals(3, table.getRowAt(0).size());
    }

    @Test
    void testAddRowWithDataArray() {
        table.addColumn(new UITableColumn("Col 1"));
        table.addColumn(new UITableColumn("Col 2"));
        table.addColumn(new UITableColumn("Col 3"));

        Object[] data = {"A", "B", "C"};
        table.addRow(data);

        assertEquals(1, table.getRowCount());
        assertEquals("A", table.getCellAt(0, 0).getValue());
        assertEquals("C", table.getCellAt(0, 2).getValue());
    }

    @Test
    void testAddRowAtIndex() {
        table.addColumn(new UITableColumn("Col 1"));
        table.addColumn(new UITableColumn("Col 2"));

        table.addRow(new Object[]{"A", "B"});
        table.addRow(new Object[]{"C", "D"});
        table.addRow(0, Arrays.asList(new UITableCell("X"), new UITableCell("Y")));

        assertEquals(3, table.getRowCount());
        assertEquals("X", table.getCellAt(0, 0).getValue());
    }

    @Test
    void testRemoveRow() {
        List<UITableCell> row1 = Arrays.asList(new UITableCell("A"));
        List<UITableCell> row2 = Arrays.asList(new UITableCell("B"));
        table.addRow(row1);
        table.addRow(row2);

        table.removeRow(row1);

        assertEquals(1, table.getRowCount());
    }

    @Test
    void testRemoveRowAt() {
        table.addColumn(new UITableColumn("Col 1"));

        table.addRow(new Object[]{"A"});
        table.addRow(new Object[]{"B"});
        table.addRow(new Object[]{"C"});

        table.removeRowAt(1);

        assertEquals(2, table.getRowCount());
        assertEquals("C", table.getCellAt(1, 0).getValue());
    }

    @Test
    void testClearRows() {
        table.addRow(new Object[]{"A"});
        table.addRow(new Object[]{"B"});
        table.setSelectedRow(0);

        table.clearRows();

        assertEquals(0, table.getRowCount());
        assertEquals(-1, table.getSelectedRow());
    }

    @Test
    void testGetCellAt() {
        table.addColumn(new UITableColumn("Col 1"));
        table.addColumn(new UITableColumn("Col 2"));
        table.addRow(new Object[]{"A", "B"});

        UITableCell cell = table.getCellAt(0, 0);
        assertNotNull(cell);
        assertEquals("A", cell.getValue());

        assertNull(table.getCellAt(10, 0));
        assertNull(table.getCellAt(0, 10));
    }

    @Test
    void testSetCellAt() {
        table.addColumn(new UITableColumn("Col 1"));
        table.addRow(new Object[]{"A"});

        UITableCell newCell = new UITableCell("New Value");
        table.setCellAt(0, 0, newCell);

        assertEquals("New Value", table.getCellAt(0, 0).getValue());
    }

    @Test
    void testSetSelectedRow() {
        table.addRow(new Object[]{"A"});
        table.addRow(new Object[]{"B"});

        final int[] selectedRow = {-1};
        table.setListener(new UITable.TableListener() {
            @Override
            public void onRowSelected(UITable table, int rowIndex) {
                selectedRow[0] = rowIndex;
            }

            @Override
            public void onCellClicked(UITable table, int rowIndex, int columnIndex) {}

            @Override
            public void onHeaderClicked(UITable table, int columnIndex) {}
        });

        table.setSelectedRow(1);

        assertEquals(1, table.getSelectedRow());
        assertEquals(1, selectedRow[0]);
    }

    @Test
    void testSetShowHeader() {
        table.setShowHeader(false);
        assertFalse(table.isShowHeader());
    }

    @Test
    void testSetShowGridLines() {
        table.setShowGridLines(false);
        assertFalse(table.isShowGridLines());
    }

    @Test
    void testSetRowHeight() {
        table.setRowHeight(40);
        assertEquals(40, table.getRowHeight(), 0.01f);
    }

    @Test
    void testSetHeaderHeight() {
        table.setHeaderHeight(40);
        assertEquals(40, table.getHeaderHeight(), 0.01f);
    }

    @Test
    void testSetColors() {
        UIColor headerColor = UIColor.GRAY;
        UIColor rowColor = UIColor.BLUE;
        UIColor selectedRowColor = UIColor.GREEN;
        UIColor gridColor = UIColor.YELLOW;
        UIColor textColor = UIColor.WHITE;

        table.setHeaderColor(headerColor);
        table.setRowColor(rowColor);
        table.setSelectedRowColor(selectedRowColor);
        table.setGridColor(gridColor);
        table.setTextColor(textColor);

        assertEquals(headerColor, table.getHeaderColor());
        assertEquals(rowColor, table.getRowColor());
        assertEquals(selectedRowColor, table.getSelectedRowColor());
        assertEquals(gridColor, table.getGridColor());
        assertEquals(textColor, table.getTextColor());
    }

    @Test
    void testGetColumnWidth() {
        table.addColumn(new UITableColumn("Column", 150));
        table.addRow(new Object[]{"Short"});
        table.addRow(new Object[]{"Very Long Text"});

        float width = table.getColumnWidth(0);
        assertTrue(width >= 150);
    }

    @Test
    void testGetColumnX() {
        table.addColumn(new UITableColumn("Col 1", 100));
        table.addColumn(new UITableColumn("Col 2", 150));

        assertEquals(table.getX(), table.getColumnX(0), 0.01f);
        assertEquals(table.getX() + 100, table.getColumnX(1), 0.01f);
    }

    @Test
    void testGetRowY() {
        table.addRow(new Object[]{"A"});
        table.addRow(new Object[]{"B"});

        float y0 = table.getRowY(0);
        float y1 = table.getRowY(1);

        assertTrue(y0 > y1);
    }

    @Test
    void testGetRowIndexAtY() {
        table.addRow(new Object[]{"A"});
        table.addRow(new Object[]{"B"});

        float y0 = table.getRowY(0);
        int index = table.getRowIndexAtY(y0);
        assertEquals(0, index);
    }

    @Test
    void testGetColumnIndexAtX() {
        table.addColumn(new UITableColumn("Col 1", 100));
        table.addColumn(new UITableColumn("Col 2", 100));

        int index = table.getColumnIndexAtX(table.getX() + 50);
        assertEquals(0, index);

        index = table.getColumnIndexAtX(table.getX() + 150);
        assertEquals(1, index);
    }

    @Test
    void testRender() {
        table.addColumn(new UITableColumn("Column"));
        table.addRow(new Object[]{"Data"});
        assertDoesNotThrow(() -> table.render());
    }

    @Test
    void testRenderHidden() {
        table.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> table.render());
    }

    @Test
    void testGetPreferredWidth() {
        table.addColumn(new UITableColumn("Column 1", 100));
        table.addColumn(new UITableColumn("Column 2", 150));

        float width = table.getPreferredWidth();
        assertEquals(250, width, 0.01f);
    }

    @Test
    void testGetPreferredHeight() {
        table.addRow(new Object[]{"A"});
        table.addRow(new Object[]{"B"});
        table.setRowHeight(30);
        table.setHeaderHeight(30);

        float height = table.getPreferredHeight();
        assertEquals(90, height, 0.01f); // 2 行 * 30 + 表头 30
    }

    @Test
    void testGetTotalHeight() {
        table.addRow(new Object[]{"A"});
        table.setRowHeight(30);

        float height = table.getTotalHeight();
        assertTrue(height > 0);
    }

    @Test
    void testSetColumns() {
        List<UITableColumn> columns = Arrays.asList(
            new UITableColumn("Col 1"),
            new UITableColumn("Col 2")
        );
        table.setColumns(columns);

        assertEquals(2, table.getColumnCount());
    }

    @Test
    void testSetRows() {
        List<List<UITableCell>> rows = Arrays.asList(
            Arrays.asList(new UITableCell("A")),
            Arrays.asList(new UITableCell("B"))
        );
        table.setRows(rows);

        assertEquals(2, table.getRowCount());
    }
}
