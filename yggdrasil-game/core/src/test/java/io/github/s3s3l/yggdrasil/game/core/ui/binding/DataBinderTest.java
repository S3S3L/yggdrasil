package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据绑定管理器单元测试
 */
class DataBinderTest {

    private DataBinder binder;
    private TestBean source;
    private TestBean target;

    @BeforeEach
    void setUp() {
        binder = new DataBinder();
        source = new TestBean();
        target = new TestBean();
    }

    @Test
    void testBind() {
        source.nameProperty().setValue("Source");
        binder.bind(source, "name", target, "name");
        assertEquals("Source", target.nameProperty().getValue());
    }

    @Test
    void testBindBidirectional() {
        binder.bindBidirectional(source, "name", target, "name");
        source.nameProperty().setValue("Source");
        assertEquals("Source", target.nameProperty().getValue());
        target.nameProperty().setValue("Target");
        assertEquals("Target", source.nameProperty().getValue());
    }

    @Test
    void testUnbind() {
        source.nameProperty().setValue("Source");
        binder.bind(source, "name", target, "name");
        binder.unbind(target, "name");
        source.nameProperty().setValue("Updated");
        // After unbind, target should not update
    }

    @Test
    void testUnbindAll() {
        binder.bind(source, "name", target, "name");
        binder.unbindAll();
        assertTrue(binder.getProperty(target, "name") == null);
    }

    @Test
    void testEnableDisable() {
        binder.disable();
        assertFalse(binder.isEnabled());
        binder.enable();
        assertTrue(binder.isEnabled());
    }

    @Test
    void testRegisterProperty() {
        StringProperty prop = new StringProperty("Test");
        binder.registerProperty(this, "test", prop);
        assertSame(prop, binder.getProperty(this, "test"));
    }

    @Test
    void testBindingBuilder() {
        source.nameProperty().setValue("Builder Test");
        binder.bind()
            .from(source, "name")
            .to(target, "name");
        assertEquals("Builder Test", target.nameProperty().getValue());
    }

    @Test
    void testBindingBuilderBidirectional() {
        binder.bind()
            .from(source, "name")
            .bidirectional()
            .to(target, "name");
        source.nameProperty().setValue("Source");
        assertEquals("Source", target.nameProperty().getValue());
        target.nameProperty().setValue("Target");
        assertEquals("Target", source.nameProperty().getValue());
    }

    /**
     * 测试用 Bean
     */
    private static class TestBean {
        private final StringProperty name = new StringProperty();

        public StringProperty nameProperty() {
            return name;
        }
    }
}
