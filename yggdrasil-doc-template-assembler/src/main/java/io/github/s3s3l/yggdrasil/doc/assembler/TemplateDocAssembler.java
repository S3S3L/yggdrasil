package io.github.s3s3l.yggdrasil.doc.assembler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import io.github.s3s3l.yggdrasil.doc.exception.DocGenerateException;
import io.github.s3s3l.yggdrasil.doc.node.DocNode;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class TemplateDocAssembler implements DocAssembler {
    private static final Configuration templateConfig;
    private static final StringTemplateLoader templateLoader = new StringTemplateLoader();

    static {
        templateConfig = new Configuration(Configuration.VERSION_2_3_30);
        templateConfig.setDefaultEncoding("UTF-8");
        templateConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        templateConfig.setLogTemplateExceptions(true);
        templateConfig.setWrapUncheckedExceptions(true);
        templateConfig.setFallbackOnNullLoopVariable(false);
        templateConfig.setTemplateLoader(templateLoader);
    }

    public static void registerTemplate(String name, String templateContent) {
        templateLoader.putTemplate(name, templateContent);
    }

    private String templateName;
    @Builder.Default
    private Locale locale = Locale.SIMPLIFIED_CHINESE;

    @Override
    public String toDocContent(DocNode doc) {
        try (StringWriter result = new StringWriter()) {
            Template template = templateConfig.getTemplate(templateName, locale);
            template.process(doc, result);

            return result.toString();
        } catch (IOException | TemplateException e) {
            throw new DocGenerateException(e);
        }
    }

}
