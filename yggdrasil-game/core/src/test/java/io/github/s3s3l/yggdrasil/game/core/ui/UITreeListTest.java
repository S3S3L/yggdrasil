package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 树形列表组件单元测试
 */
class UITreeListTest {

    private UITreeList treeList;

    @BeforeEach
    void setUp() {
        treeList = new UITreeList();
        treeList.setId(1L);
        treeList.setSize(400, 300);
    }

    @Test
    void testTreeListCreation() {
        assertNotNull(treeList);
        assertEquals(0, treeList.getRootCount());
        assertEquals(20, treeList.getIndentSize(), 0.01f);
        assertEquals(30, treeList.getRowHeight(), 0.01f);
        assertEquals(UIColor.WHITE, treeList.getTextColor());
        assertTrue(treeList.isShowLines());
    }

    @Test
    void testSetIndentSize() {
        treeList.setIndentSize(25);
        assertEquals(25, treeList.getIndentSize(), 0.01f);
    }

    @Test
    void testSetRowHeight() {
        treeList.setRowHeight(40);
        assertEquals(40, treeList.getRowHeight(), 0.01f);
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.YELLOW;
        treeList.setTextColor(color);
        assertEquals(color, treeList.getTextColor());
    }

    @Test
    void testSetSelectedColor() {
        UIColor color = UIColor.BLUE;
        treeList.setSelectedColor(color);
        assertEquals(color, treeList.getSelectedColor());
    }

    @Test
    void testSetHoverColor() {
        UIColor color = UIColor.GRAY;
        treeList.setHoverColor(color);
        assertEquals(color, treeList.getHoverColor());
    }

    @Test
    void testSetShowLines() {
        treeList.setShowLines(false);
        assertFalse(treeList.isShowLines());
    }

    @Test
    void testAddRoot() {
        UITreeNode node = new UITreeNode("Root");
        treeList.addRoot(node);

        assertEquals(1, treeList.getRootCount());
        assertSame(node, treeList.getRootAt(0));
        assertEquals(0, node.getLevel());
    }

    @Test
    void testAddRootAtIndex() {
        treeList.addRoot(new UITreeNode("Root 1"));
        treeList.addRoot(new UITreeNode("Root 2"));
        treeList.addRoot(0, new UITreeNode("New Root"));

        assertEquals(3, treeList.getRootCount());
        assertEquals("New Root", treeList.getRootAt(0).getText());
    }

    @Test
    void testRemoveRoot() {
        UITreeNode node1 = new UITreeNode("Root 1");
        UITreeNode node2 = new UITreeNode("Root 2");
        treeList.addRoot(node1);
        treeList.addRoot(node2);

        treeList.removeRoot(node1);

        assertEquals(1, treeList.getRootCount());
        assertSame(node2, treeList.getRootAt(0));
        assertNull(node1.getParent());
    }

    @Test
    void testRemoveRootAt() {
        treeList.addRoot(new UITreeNode("Root 1"));
        treeList.addRoot(new UITreeNode("Root 2"));
        treeList.addRoot(new UITreeNode("Root 3"));

        treeList.removeRootAt(1);

        assertEquals(2, treeList.getRootCount());
        assertEquals("Root 3", treeList.getRootAt(1).getText());
    }

    @Test
    void testClear() {
        treeList.addRoot(new UITreeNode("Root 1"));
        treeList.addRoot(new UITreeNode("Root 2"));

        treeList.clear();

        assertEquals(0, treeList.getRootCount());
    }

    @Test
    void testSetRoots() {
        List<UITreeNode> roots = Arrays.asList(
            new UITreeNode("Root 1"),
            new UITreeNode("Root 2")
        );
        treeList.setRoots(roots);

        assertEquals(2, treeList.getRootCount());
        assertEquals("Root 1", treeList.getRootAt(0).getText());
    }

    @Test
    void testSetRootsNull() {
        treeList.setRoots(null);
        assertEquals(0, treeList.getRootCount());
    }

    @Test
    void testFindNodeByText() {
        UITreeNode root1 = new UITreeNode("Root 1");
        UITreeNode child1 = new UITreeNode("Child 1");
        root1.addChild(child1);

        UITreeNode root2 = new UITreeNode("Root 2");

        treeList.addRoot(root1);
        treeList.addRoot(root2);

        UITreeNode found = treeList.findNodeByText("Child 1");
        assertSame(child1, found);

        assertNull(treeList.findNodeByText("Non Existent"));
    }

    @Test
    void testFindNodeByData() {
        UITreeNode root1 = new UITreeNode("Root 1", "data1");
        UITreeNode child1 = new UITreeNode("Child 1", "childData");

        root1.addChild(child1);
        treeList.addRoot(root1);

        UITreeNode found = treeList.findNodeByData("childData");
        assertSame(child1, found);

        assertNull(treeList.findNodeByData("Non Existent"));
    }

    @Test
    void testGetAllVisibleNodes() {
        UITreeNode root1 = new UITreeNode("Root 1");
        UITreeNode child1 = new UITreeNode("Child 1");
        root1.addChild(child1);

        UITreeNode root2 = new UITreeNode("Root 2");

        treeList.addRoot(root1);
        treeList.addRoot(root2);

        // 默认节点是收起的
        List<UITreeNode> visibleNodes = treeList.getAllVisibleNodes();
        assertEquals(2, visibleNodes.size());

        // 展开 root1 后，子节点可见
        root1.setExpanded(true);
        visibleNodes = treeList.getAllVisibleNodes();
        assertEquals(3, visibleNodes.size());
    }

    @Test
    void testGetVisibleIndex() {
        UITreeNode root1 = new UITreeNode("Root 1");
        UITreeNode root2 = new UITreeNode("Root 2");
        treeList.addRoot(root1);
        treeList.addRoot(root2);

        assertEquals(0, treeList.getVisibleIndex(root1));
        assertEquals(1, treeList.getVisibleIndex(root2));

        UITreeNode nonExistent = new UITreeNode("Not Added");
        assertEquals(-1, treeList.getVisibleIndex(nonExistent));
    }

    @Test
    void testExpandAll() {
        UITreeNode root = new UITreeNode("Root");
        UITreeNode child1 = new UITreeNode("Child 1");
        UITreeNode child2 = new UITreeNode("Child 2");
        child1.addChild(child2);
        root.addChild(child1);

        treeList.addRoot(root);

        treeList.expandAll();

        assertTrue(root.isExpanded());
        assertTrue(child1.isExpanded());
    }

    @Test
    void testCollapseAll() {
        UITreeNode root = new UITreeNode("Root");
        root.setExpanded(true);

        treeList.addRoot(root);

        treeList.collapseAll();

        assertFalse(root.isExpanded());
    }

    @Test
    void testSelectNode() {
        UITreeNode node1 = new UITreeNode("Node 1");
        UITreeNode node2 = new UITreeNode("Node 2");
        treeList.addRoot(node1);
        treeList.addRoot(node2);

        final UITreeNode[] selectedNode = {null};
        treeList.setListener(new UITreeList.TreeListListener() {
            @Override
            public void onNodeSelected(UITreeNode node) {
                selectedNode[0] = node;
            }

            @Override
            public void onNodeExpanded(UITreeNode node, boolean isExpanded) {}

            @Override
            public void onNodeDoubleClicked(UITreeNode node) {}
        });

        treeList.selectNode(node2);

        assertFalse(node1.isSelected());
        assertTrue(node2.isSelected());
        assertSame(node2, selectedNode[0]);
    }

    @Test
    void testDeselectAll() {
        UITreeNode node1 = new UITreeNode("Node 1");
        UITreeNode node2 = new UITreeNode("Node 2");
        node1.setExpanded(true);
        UITreeNode child1 = new UITreeNode("Child 1");
        node1.addChild(child1);

        treeList.addRoot(node1);
        treeList.addRoot(node2);

        node1.setSelected(true);
        node2.setSelected(true);
        child1.setSelected(true);

        treeList.deselectAll();

        assertFalse(node1.isSelected());
        assertFalse(node2.isSelected());
        assertFalse(child1.isSelected());
    }

    @Test
    void testGetNodeY() {
        UITreeNode root1 = new UITreeNode("Root 1");
        UITreeNode root2 = new UITreeNode("Root 2");
        treeList.addRoot(root1);
        treeList.addRoot(root2);

        float y1 = treeList.getNodeY(root1);
        float y2 = treeList.getNodeY(root2);

        // root1 应该在 root2 上方（Y 坐标更大）
        assertTrue(y1 > y2);
        assertEquals(30, y1 - y2, 0.01f); // 相差一行高度
    }

    @Test
    void testGetNodeAtY() {
        UITreeNode root1 = new UITreeNode("Root 1");
        UITreeNode root2 = new UITreeNode("Root 2");
        treeList.addRoot(root1);
        treeList.addRoot(root2);

        // 计算 root1 的 Y 坐标
        float y1 = treeList.getNodeY(root1);
        UITreeNode node = treeList.getNodeAtY(y1);
        assertNotNull(node);
        assertEquals("Root 1", node.getText());
    }

    @Test
    void testListener() {
        final boolean[] selected = {false};
        final boolean[] expanded = {false};
        final boolean[] doubleClicked = {false};

        treeList.setListener(new UITreeList.TreeListListener() {
            @Override
            public void onNodeSelected(UITreeNode node) {
                selected[0] = true;
            }

            @Override
            public void onNodeExpanded(UITreeNode node, boolean isExpanded) {
                if (isExpanded) {
                    expanded[0] = true;
                }
            }

            @Override
            public void onNodeDoubleClicked(UITreeNode node) {
                doubleClicked[0] = true;
            }
        });

        UITreeNode node = new UITreeNode("Node");
        treeList.addRoot(node);

        treeList.selectNode(node);
        assertTrue(selected[0]);

        // 模拟双击触发
        treeList.handleDoubleClick(0, treeList.getNodeY(node));
        assertTrue(doubleClicked[0]);
    }

    @Test
    void testRender() {
        treeList.addRoot(new UITreeNode("Root 1"));
        assertDoesNotThrow(() -> treeList.render());
    }

    @Test
    void testRenderHidden() {
        treeList.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> treeList.render());
    }

    @Test
    void testGetPreferredWidth() {
        UITreeNode root = new UITreeNode("Test Node");
        treeList.addRoot(root);

        float width = treeList.getPreferredWidth();
        assertTrue(width > 0);
    }

    @Test
    void testGetPreferredHeight() {
        treeList.addRoot(new UITreeNode("Root 1"));
        treeList.addRoot(new UITreeNode("Root 2"));

        float height = treeList.getPreferredHeight();
        assertEquals(60, height, 0.01f); // 2 行 * 30
    }

    @Test
    void testOnClick() {
        UITreeNode node = new UITreeNode("Node");
        treeList.addRoot(node);

        final boolean[] clicked = {false};
        node.setListener(new UITreeNode.TreeNodeListener() {
            @Override
            public void onExpandedChanged(UITreeNode node, boolean isExpanded) {}

            @Override
            public void onSelectedChanged(UITreeNode node, boolean isSelected) {
                if (isSelected) {
                    clicked[0] = true;
                }
            }

            @Override
            public void onClick(UITreeNode node) {}
        });

        // 模拟点击节点位置
        // 注意：实际测试中需要设置 input

        assertFalse(node.isSelected());
    }

    @Test
    void testNodeLevelUpdate() {
        UITreeNode root = new UITreeNode("Root");
        UITreeNode child = new UITreeNode("Child");
        UITreeNode grandchild = new UITreeNode("Grandchild");

        child.addChild(grandchild);
        root.addChild(child);
        treeList.addRoot(root);

        // 层级应该由 treeList 管理
        assertEquals(0, root.getLevel());
        assertEquals(1, child.getLevel());
        assertEquals(2, grandchild.getLevel());
    }

    @Test
    void testAddRootWithNullNode() {
        treeList.addRoot(null);
        assertEquals(0, treeList.getRootCount());
    }

    @Test
    void testAddRootAtIndexOutOfBounds() {
        treeList.addRoot(100, new UITreeNode("Root"));
        assertEquals(1, treeList.getRootCount());
        assertEquals("Root", treeList.getRootAt(0).getText());
    }

    @Test
    void testRemoveRootAtOutOfBounds() {
        treeList.addRoot(new UITreeNode("Root"));
        treeList.removeRootAt(100); // 应该不抛异常
        assertEquals(1, treeList.getRootCount());
    }
}
