package io.github.s3s3l.yggdrasil.template.freemarker;

import io.github.s3s3l.yggdrasil.template.AbstractTemplateManager;
import io.github.s3s3l.yggdrasil.template.TemplateMeta;
import io.github.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import lombok.Getter;

public class FreeMarkerTemplateManager extends AbstractTemplateManager {
    public static final String DEFAULT_FILE_PATTERN = "glob:**/*.ftl";

    @Getter
    private final FreeMarkerHelper helper;

    public FreeMarkerTemplateManager() {
        super(DEFAULT_FILE_PATTERN);
        this.helper = new FreeMarkerHelper();
    }

    public FreeMarkerTemplateManager(String filePattern) {
        super(filePattern);
        this.helper = new FreeMarkerHelper();
    }

    public FreeMarkerTemplateManager(FreeMarkerHelper helper) {
        super(DEFAULT_FILE_PATTERN);
        this.helper = helper;
    }

    public FreeMarkerTemplateManager(String filePattern, FreeMarkerHelper helper) {
        super(filePattern);
        this.helper = helper;
    }

    @Override
    protected String doCompile(TemplateMeta template, Object data) {
        return helper.format(String.format("%s_%d", template.getName(), template
                .getPriority()), template.getContent(),
                data);
    }
}
