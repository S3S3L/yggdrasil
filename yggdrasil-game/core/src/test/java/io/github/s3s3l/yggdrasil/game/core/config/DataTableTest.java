package io.github.s3s3l.yggdrasil.game.core.config;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据表单元测试
 */
class DataTableTest {

    private DataTable<TestData> table;

    @BeforeEach
    void setUp() {
        table = new DataTable<>("test_table", TestData.class);
    }

    @Test
    void testTableCreation() {
        assertEquals("test_table", table.getName());
        assertEquals(TestData.class, table.getType());
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }

    @Test
    void testAddAndGet() {
        TestData item = new TestData(1L, "Item 1", "Description 1");
        table.add(item);

        assertEquals(1, table.size());
        assertFalse(table.isEmpty());
        assertTrue(table.contains(1L));

        TestData retrieved = table.get(1L);
        assertNotNull(retrieved);
        assertEquals("Item 1", retrieved.getName());
    }

    @Test
    void testRemove() {
        TestData item = new TestData(1L, "Item 1", "Description 1");
        table.add(item);

        TestData removed = table.remove(1L);
        assertNotNull(removed);
        assertEquals(0, table.size());
        assertFalse(table.contains(1L));
    }

    @Test
    void testGetAll() {
        table.add(new TestData(1L, "Item 1", "Description 1"));
        table.add(new TestData(2L, "Item 2", "Description 2"));
        table.add(new TestData(3L, "Item 3", "Description 3"));

        var all = table.getAll();
        assertEquals(3, all.size());
    }

    @Test
    void testFilter() {
        table.add(new TestData(1L, "Item 1", "Description 1"));
        table.add(new TestData(2L, "Item 2", "Description 2"));
        table.add(new TestData(3L, "Item 3", "Description 3"));

        List<TestData> filtered = table.filter(item -> item.getId() > 1L);
        assertEquals(2, filtered.size());
    }

    @Test
    void testFindFirst() {
        table.add(new TestData(1L, "Item 1", "Description 1"));
        table.add(new TestData(2L, "Item 2", "Description 2"));
        table.add(new TestData(3L, "Item 3", "Description 3"));

        TestData found = table.findFirst(item -> "Item 2".equals(item.getName()));
        assertNotNull(found);
        assertEquals(2L, found.getId());
    }

    @Test
    void testFindFirstNotFound() {
        table.add(new TestData(1L, "Item 1", "Description 1"));

        TestData found = table.findFirst(item -> "Nonexistent".equals(item.getName()));
        assertNull(found);
    }

    @Test
    void testClear() {
        table.add(new TestData(1L, "Item 1", "Description 1"));
        table.add(new TestData(2L, "Item 2", "Description 2"));

        table.clear();

        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }

    @Test
    void testToList() {
        table.add(new TestData(1L, "Item 1", "Description 1"));
        table.add(new TestData(2L, "Item 2", "Description 2"));

        List<TestData> list = table.toList();
        assertEquals(2, list.size());
    }

    @Test
    void testToMap() {
        table.add(new TestData(1L, "Item 1", "Description 1"));
        table.add(new TestData(2L, "Item 2", "Description 2"));

        var map = table.toMap();
        assertEquals(2, map.size());
        assertTrue(map.containsKey(1L));
        assertTrue(map.containsKey(2L));
    }

    @Test
    void testIterator() {
        table.add(new TestData(1L, "Item 1", "Description 1"));
        table.add(new TestData(2L, "Item 2", "Description 2"));

        int count = 0;
        for (TestData item : table) {
            count++;
        }
        assertEquals(2, count);
    }

    // 测试用的数据类
    private static class TestData implements ID {
        private long numericId;
        private String name;
        private String description;

        TestData(long id, String name, String description) {
            this.numericId = id;
            this.name = name;
            this.description = description;
        }

        @Override
        public long getId() {
            return numericId;
        }

        @Override
        public void setId(long id) {
            this.numericId = id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
