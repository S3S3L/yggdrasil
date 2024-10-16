package io.github.s3s3l.yggdrasil.template;

public interface TemplateManager {

    void registerResources(TemplateResource... resources);

    <T> String compile(T data, Class<T> type);
}
