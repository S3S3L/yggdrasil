
package io.github.s3s3l.yggdrasil.template.freemarker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.template.Template;
import io.github.s3s3l.yggdrasil.template.TemplateResource;
import io.github.s3s3l.yggdrasil.template.exception.TemplateNotFoundException;
import io.github.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class FreeMarkerTemplateManagerTest {

    private FreeMarkerHelper mockHelper = new FreeMarkerHelper();

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @Template("test")
    public static class TestModule {
        private String name;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestModuleNoAnnotation {
        private String name;
    }

    @Test
    public void testDefaultConstructor() {
        FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager();
        Assertions.assertNotNull(manager);
        Assertions.assertEquals(FreeMarkerTemplateManager.DEFAULT_FILE_PATTERN, manager.getFilePattern());
        Assertions.assertNotNull(manager.getHelper());
    }

    @Test
    public void testConstructorWithFilePattern() {
        String filePattern = "customPattern";
        FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager(filePattern);
        Assertions.assertNotNull(manager);
        Assertions.assertEquals(filePattern, manager.getFilePattern());
        Assertions.assertNotNull(manager.getHelper());
    }

    @Test
    public void testConstructorWithHelper() {
        FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager(mockHelper);
        Assertions.assertNotNull(manager);
        Assertions.assertEquals(FreeMarkerTemplateManager.DEFAULT_FILE_PATTERN, manager.getFilePattern());
        Assertions.assertEquals(mockHelper, manager.getHelper());
    }

    @Test
    public void testConstructorWithFilePatternAndHelper() {
        String filePattern = "customPattern";
        FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager(filePattern, mockHelper);
        Assertions.assertNotNull(manager);
        Assertions.assertEquals(filePattern, manager.getFilePattern());
        Assertions.assertEquals(mockHelper, manager.getHelper());
    }

    @Test
    public void testCompile() {
        FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager();
        manager.registerResources(new TemplateResource(0, FileUtils.getFirstExistResourcePath("classpath:.")));
        String result = manager.compile(TestModule.builder().name("compile").build(), TestModule.class);

        Assertions.assertEquals("test for compile", result.trim());
    }

    @Test
    public void testCompile_MultiPriority() throws InterruptedException {
        FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager();
        manager.registerResources(new TemplateResource(0, FileUtils.getFirstExistResourcePath("classpath:common")),
                new TemplateResource(-1, FileUtils.getFirstExistResourcePath("classpath:low")));

        Assertions.assertEquals("test for compile",
                manager.compile(TestModule.builder().name("compile").build(), TestModule.class).trim());

        manager.registerResources(new TemplateResource(1, FileUtils.getFirstExistResourcePath("classpath:high")));

        Assertions.assertEquals("test for compile high priority",
                manager.compile(TestModule.builder().name("compile").build(), TestModule.class).trim());
    }

    @Test
    public void testTemplateNotFound_NoResource() {
        FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager();

        Assertions.assertThrows(
                TemplateNotFoundException.class, () -> manager
                        .compile(TestModule.builder().name("compile").build(), TestModule.class));
    }

    @Test
    public void testTemplateNotFound_NoAnnotation() {
        FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager();
        manager.registerResources(new TemplateResource(0, FileUtils.getFirstExistResourcePath("classpath:.")));

        Assertions.assertThrows(
                TemplateNotFoundException.class, () -> manager
                        .compile(TestModuleNoAnnotation.builder().name("compile").build(),
                                TestModuleNoAnnotation.class));
    }
}