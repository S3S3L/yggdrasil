package io.github.s3s3l.yggdrasil.template;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.bean.exception.ResourceProcessException;
import io.github.s3s3l.yggdrasil.template.exception.TemplateNotFoundException;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplateManager implements TemplateManager {
    protected final Map<String, TemplateMeta> templateMetas = new ConcurrentHashMap<>();
    protected final Map<Class<?>, String> typeToNameMap = new ConcurrentHashMap<>();
    @Getter
    protected final String filePattern;
    protected boolean lazyLoad = true;

    public AbstractTemplateManager(String filePattern) {
        this.filePattern = filePattern;
    }

    @Override
    public void registerResources(TemplateResource... resources) {
        if (resources == null) {
            return;
        }

        for (TemplateResource resource : resources) {
            if (resource == null) {
                continue;
            }

            if (StringUtils.isEmpty(resource.getPath())) {
                log.warn("Template resource path is empty. Resource: {}", resource);
                continue;
            }

            try {
                List<Path> matchedFiles = FileUtils.findMatchedFiles(Path.of(resource.getPath()), filePattern);
                for (Path file : matchedFiles) {
                    TemplateMeta meta = TemplateMeta.builder()
                            .priority(resource.getPriority())
                            .name(FileUtils.getFileNameWithoutExtension(file.getFileName().toString()))
                            .path(file)
                            .build();
                    templateMetas.compute(meta.getName(), (name, oldMeta) -> {
                        if (oldMeta != null && oldMeta.getPriority() > meta.getPriority()) {
                            return oldMeta;
                        } else {
                            if (!lazyLoad) {
                                loadTemplateContent(meta);
                            }
                            return meta;
                        }
                    });
                }
            } catch (IOException e) {
                throw new ResourceProcessException("Template resource load error. Resource: " + resource, e);
            }
        }
    }

    protected String buildRealFilePattern(String basePath) {
        String[] parts = filePattern.split(":");
        if (parts.length == 1) {
            return String.join(File.pathSeparator, basePath, filePattern);
        }
        return String.format("%s:%s%s", parts[0], basePath,
                String.join("", Arrays.copyOfRange(parts, 1, parts.length)));
    }

    protected static String buildRealFilePatternTest(String basePath, String filePattern) {
        String[] parts = filePattern.split(":");
        if (parts.length == 1) {
            return String.join(File.pathSeparator, basePath, filePattern);
        }
        return String.format("%s:%s%s", parts[0], basePath,
                String.join("", Arrays.copyOfRange(parts, 1, parts.length)));
    }

    protected void loadTemplateContent(TemplateMeta meta) {
        if (meta.isLoaded()) {
            return;
        }

        try {
            String content = FileUtils.readToEnd(meta.getPath().toString(), StandardCharsets.UTF_8);
            meta.setContent(content);
            meta.setLoaded(true);
        } catch (IOException e) {
            throw new ResourceProcessException("Template content load error. Meta: " + meta, e);
        }
    }

    @Override
    public <T> String compile(T data) {
        Class<?> type = data.getClass();
        String templateName = typeToNameMap.computeIfAbsent(type, k -> {
            TemplateMeta templateMeta = findTemplate(k);
            if (templateMeta == null) {
                throw new TemplateNotFoundException("No template found for type: " + type);
            }
            return templateMeta.getName();
        });

        return doCompile(findTemplateAndLoadContent(templateName), data);
    }

    protected TemplateMeta findTemplate(Class<?> type) {
        Template template = ReflectionUtils.getAnnotation(type, Template.class);

        if (template == null) {
            throw new TemplateNotFoundException(type + " has no @Template annotation.");
        }

        return templateMetas.get(template.value());
    }

    protected TemplateMeta findTemplateAndLoadContent(String templateName) {
        TemplateMeta templateMeta = templateMetas.compute(templateName, (name, meta) -> {
            if (meta == null) {
                throw new TemplateNotFoundException("No template found: " + name);
            }

            loadTemplateContent(meta);
            return meta;
        });

        return templateMeta;
    }

    protected abstract String doCompile(TemplateMeta template, Object data);
}
