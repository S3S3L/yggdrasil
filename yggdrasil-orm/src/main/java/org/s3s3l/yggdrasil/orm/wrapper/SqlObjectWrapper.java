package org.s3s3l.yggdrasil.orm.wrapper;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

public class SqlObjectWrapper extends DefaultObjectWrapper {

    public SqlObjectWrapper(Version incompatibleImprovements) {
        super(incompatibleImprovements);
    }

    @Override
    public TemplateModel wrap(Object obj) throws TemplateModelException {
        if (obj instanceof String) {
            return super.wrap(String.format("'%s'", obj));
        }
        return super.wrap(obj);
    }
    
}
