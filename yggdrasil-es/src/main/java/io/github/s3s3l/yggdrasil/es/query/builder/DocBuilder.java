package io.github.s3s3l.yggdrasil.es.query.builder;

import io.github.s3s3l.yggdrasil.es.query.Doc;
import io.github.s3s3l.yggdrasil.es.query.Esdsl;

/**
 * <p>
 * </p>
 * ClassName:DocBuilder <br>
 * Date: Jan 7, 2019 11:22:25 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
// TODO 未完成
public class DocBuilder implements DocBeanBuilder, DocFieldBuilder {
    private EsdslBuilder preBuilder;
    private Doc doc;

    public DocBuilder(EsdslBuilder builder, Doc doc) {
        this.doc = doc;
        this.preBuilder = builder;
    }

    @Override
    public DocFieldBuilder field(String name, String value) {
        return null;
    }

    @Override
    public EsdslBuilder done() {
        return null;
    }

    @Override
    public Esdsl bean(Object bean) {
        return null;
    }
}
