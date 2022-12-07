package io.github.s3s3l.yggdrasil.utils.common;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.github.s3s3l.yggdrasil.bean.exception.ResourceProcessException;
import io.github.s3s3l.yggdrasil.utils.security.SecurityUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerHelper {
    private final Configuration templateConfig;
    private final StringTemplateLoader templateLoader = new StringTemplateLoader();

    public FreeMarkerHelper() {
        templateConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        templateConfig.setDefaultEncoding("UTF-8");
        templateConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        templateConfig.setLogTemplateExceptions(true);
        templateConfig.setWrapUncheckedExceptions(true);
        templateConfig.setFallbackOnNullLoopVariable(false);
        templateConfig.setTemplateLoader(templateLoader);
    }

    public FreeMarkerHelper config(Consumer<Configuration> consumer) {
        consumer.accept(templateConfig);
        return this;
    }

    public String format(String templateName, String format, Object data) {
        return format(templateName, () -> format, data);
    }

    public String format(String templateName, Supplier<String> formSupplier, Object data) {
        try (StringWriter result = new StringWriter()) {

            if (templateLoader.findTemplateSource(templateName) == null) {
                templateLoader.putTemplate(templateName, formSupplier.get());
            }
            Template template = templateConfig.getTemplate(templateName, Locale.CHINESE);
            template.process(data, result);

            return result.toString();
        } catch (IOException | TemplateException e) {
            throw new ResourceProcessException(e);
        }
    }

    public String format(String format, Object data) {
        return format(SecurityUtils.getMD5(format), () -> format, data);
    }

    public void addTemplate(String templateName, String format) {
        templateLoader.putTemplate(templateName, format);
    }
}
