package io.github.s3s3l.yggdrasil.utils.reflect.scan;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClassScannerTest {

    private ClassScanner classScanner;

    @BeforeEach
    public void setUp() {
        classScanner = new ClassScanner();
    }

    @Test
    public void testScanPackageWithClasses() {
        Set<Class<?>> classes = classScanner.scan("io.github.s3s3l.yggdrasil.utils.reflect.scan");
        assertFalse(classes.isEmpty());
        assertTrue(classes.contains(ClassScanner.class));
    }

    @Test
    public void testScanPackageWithoutClasses() {
        Set<Class<?>> classes = classScanner.scan("io.github.s3s3l.yggdrasil.utils.reflect.nonexistent");
        assertTrue(classes.isEmpty());
    }
}