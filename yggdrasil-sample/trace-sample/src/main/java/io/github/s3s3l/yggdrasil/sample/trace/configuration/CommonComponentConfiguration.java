package io.github.s3s3l.yggdrasil.sample.trace.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.s3s3l.yggdrasil.template.TemplateManager;
import io.github.s3s3l.yggdrasil.template.TemplateResource;
import io.github.s3s3l.yggdrasil.template.freemarker.FreeMarkerTemplateManager;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;

@Configuration
public class CommonComponentConfiguration {
    
    @Bean
    TemplateManager templateManager() {
        TemplateManager templateManager = new FreeMarkerTemplateManager();
        templateManager.registerResources(new TemplateResource(0, FileUtils.getFirstExistResourcePath("classpath:es")));

        return templateManager;
    }
}
