package io.github.s3s3l.yggdrasil.utils.reflect;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class ReflectionsHelperTest {
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface AnotherTestAnnotation {
    }

    public interface TestInterface {
    }

    @TestAnnotation
    public class TestClass implements TestInterface {
        @TestAnnotation
        public void testMethod() {
        }
    }

    @Test
    public void testGetMethodAnnotatedWith() {
        Set<Method> methods = ReflectionsHelper.getMethodAnnotatedWith(TestAnnotation.class,
                "io.github.s3s3l.yggdrasil.utils.reflect");
        assertNotNull(methods);
        assertFalse(methods.isEmpty());
    }

    @Test
    public void testGetSubTypesOf() {
        Set<Class<? extends TestInterface>> subTypes = ReflectionsHelper.getSubTypesOf(TestInterface.class,
                "io.github.s3s3l.yggdrasil.utils.reflect");
        assertNotNull(subTypes);
        assertFalse(subTypes.isEmpty());
    }

    @Test
    public void testGetAllTypes() {
        Set<Class<?>> allTypes = ReflectionsHelper.getAllTypes("io.github.s3s3l.yggdrasil.utils.reflect");
        assertNotNull(allTypes);
        assertFalse(allTypes.isEmpty());
    }

    @Test
    public void testGetTypesAnnotatedWithSingleAnnotation() {
        Set<Class<?>> annotatedTypes = ReflectionsHelper.getTypesAnnotatedWith(TestAnnotation.class,
                "io.github.s3s3l.yggdrasil.utils.reflect");
        assertNotNull(annotatedTypes);
        assertFalse(annotatedTypes.isEmpty());
    }

    @Test
    public void testGetTypesAnnotatedWithMultipleAnnotations() {
        List<Class<? extends Annotation>> annotations = Arrays.asList(TestAnnotation.class,
                AnotherTestAnnotation.class);
        Set<Class<?>> annotatedTypes = ReflectionsHelper.getTypesAnnotatedWith(annotations, false,
                "io.github.s3s3l.yggdrasil.utils.reflect");
        assertNotNull(annotatedTypes);
        assertFalse(annotatedTypes.isEmpty());
    }
}