package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 输入复用器单元测试
 */
class InputMultiplexerTest {

    private InputMultiplexer multiplexer;

    @BeforeEach
    void setUp() {
        multiplexer = new InputMultiplexer();
    }

    @Test
    void testAddProcessor() {
        TestProcessor processor = new TestProcessor();
        multiplexer.addProcessor(processor);

        assertTrue(multiplexer.getProcessors().contains(processor));
    }

    @Test
    void testAddProcessorAtIndex() {
        TestProcessor processor1 = new TestProcessor();
        TestProcessor processor2 = new TestProcessor();

        multiplexer.addProcessor(processor1);
        multiplexer.addProcessor(0, processor2);

        assertEquals(processor2, multiplexer.getProcessors().get(0));
        assertEquals(processor1, multiplexer.getProcessors().get(1));
    }

    @Test
    void testRemoveProcessor() {
        TestProcessor processor = new TestProcessor();
        multiplexer.addProcessor(processor);
        multiplexer.removeProcessor(processor);

        assertFalse(multiplexer.getProcessors().contains(processor));
    }

    @Test
    void testClearProcessors() {
        multiplexer.addProcessor(new TestProcessor());
        multiplexer.addProcessor(new TestProcessor());
        multiplexer.clearProcessors();

        assertTrue(multiplexer.getProcessors().isEmpty());
    }

    @Test
    void testGetProcessorsReturnsCopy() {
        TestProcessor processor = new TestProcessor();
        multiplexer.addProcessor(processor);

        var processors = multiplexer.getProcessors();
        processors.clear();

        // 原始处理器不受影响
        assertEquals(1, multiplexer.getProcessors().size());
    }

    @Test
    void testTouchDownPropagation() {
        TestProcessor processor1 = new TestProcessor();
        TestProcessor processor2 = new TestProcessor();

        multiplexer.addProcessor(processor1);
        multiplexer.addProcessor(processor2);

        multiplexer.touchDown(100f, 100f, 0);

        assertTrue(processor1.touchDownCalled);
        assertTrue(processor2.touchDownCalled);
    }

    @Test
    void testTouchDownStopsOnHandled() {
        TestProcessor processor1 = new TestProcessor();
        processor1.handleAll = true; // 第一个处理器处理所有事件

        TestProcessor processor2 = new TestProcessor();

        multiplexer.addProcessor(processor1);
        multiplexer.addProcessor(processor2);

        multiplexer.touchDown(100f, 100f, 0);

        assertTrue(processor1.touchDownCalled);
        assertFalse(processor2.touchDownCalled); // 不应该被调用
    }

    @Test
    void testCatchUnhandled() {
        TestProcessor processor1 = new TestProcessor();
        TestProcessor processor2 = new TestProcessor();

        multiplexer = new InputMultiplexer(true); // 捕获未处理事件
        multiplexer.addProcessor(processor1);
        multiplexer.addProcessor(processor2);

        multiplexer.touchDown(100f, 100f, 0);

        assertTrue(processor1.touchDownCalled);
        assertTrue(processor2.touchDownCalled); // 即使未处理也会调用
    }

    @Test
    void testTouchUpPropagation() {
        TestProcessor processor1 = new TestProcessor();
        TestProcessor processor2 = new TestProcessor();

        multiplexer.addProcessor(processor1);
        multiplexer.addProcessor(processor2);

        multiplexer.touchUp(100f, 100f, 0);

        assertTrue(processor1.touchUpCalled);
        assertTrue(processor2.touchUpCalled);
    }

    @Test
    void testTouchDraggedPropagation() {
        TestProcessor processor1 = new TestProcessor();
        TestProcessor processor2 = new TestProcessor();

        multiplexer.addProcessor(processor1);
        multiplexer.addProcessor(processor2);

        multiplexer.touchDragged(100f, 100f, 0);

        assertTrue(processor1.touchDraggedCalled);
        assertTrue(processor2.touchDraggedCalled);
    }

    @Test
    void testKeyDownPropagation() {
        TestProcessor processor1 = new TestProcessor();
        TestProcessor processor2 = new TestProcessor();

        multiplexer.addProcessor(processor1);
        multiplexer.addProcessor(processor2);

        multiplexer.keyDown(65);

        assertTrue(processor1.keyDownCalled);
        assertTrue(processor2.keyDownCalled);
    }

    @Test
    void testKeyUpPropagation() {
        TestProcessor processor1 = new TestProcessor();
        TestProcessor processor2 = new TestProcessor();

        multiplexer.addProcessor(processor1);
        multiplexer.addProcessor(processor2);

        multiplexer.keyUp(65);

        assertTrue(processor1.keyUpCalled);
        assertTrue(processor2.keyUpCalled);
    }

    @Test
    void testSetCatchUnhandled() {
        assertFalse(multiplexer.isCatchUnhandled());
        multiplexer.setCatchUnhandled(true);
        assertTrue(multiplexer.isCatchUnhandled());
    }

    @Test
    void testAddNullProcessor() {
        // 不应该抛出异常
        multiplexer.addProcessor(null);
        multiplexer.addProcessor(0, null);
        assertTrue(multiplexer.getProcessors().isEmpty());
    }

    private static class TestProcessor implements InputProcessor {
        boolean touchDownCalled;
        boolean touchUpCalled;
        boolean touchDraggedCalled;
        boolean keyDownCalled;
        boolean keyUpCalled;
        boolean handleAll = false;

        @Override
        public boolean touchDown(float x, float y, int pointerId) {
            touchDownCalled = true;
            return handleAll;
        }

        @Override
        public boolean touchUp(float x, float y, int pointerId) {
            touchUpCalled = true;
            return handleAll;
        }

        @Override
        public boolean touchDragged(float x, float y, int pointerId) {
            touchDraggedCalled = true;
            return handleAll;
        }

        @Override
        public boolean keyDown(int keyCode) {
            keyDownCalled = true;
            return handleAll;
        }

        @Override
        public boolean keyUp(int keyCode) {
            keyUpCalled = true;
            return handleAll;
        }
    }
}
