package io.github.s3s3l.yggdrasil.es.query.builder;

import io.github.s3s3l.yggdrasil.es.query.BooleanQuery;
import io.github.s3s3l.yggdrasil.es.query.Doc;
import io.github.s3s3l.yggdrasil.es.query.Esdsl;
import io.github.s3s3l.yggdrasil.es.query.QueryBody;
import io.github.s3s3l.yggdrasil.es.query.Sort;

/**
 * <p>
 * </p>
 * ClassName:ESQuery <br>
 * Date: Jan 2, 2019 11:49:54 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */

public class EsdslBuilder implements Builder {
    private QueryBody query;
    private Sort sort;
    private Doc doc;
    private final Esdsl dsl = new Esdsl();

    public BooleanQuerySelector bool() {
        if (query == null) {
            query = new QueryBody(new BooleanQuery());
            dsl.add(query);
        }
        return new BooleanQueryBuilder(this, query.getBool());
    }

    public SortSelector sort() {
        if (sort == null) {
            sort = new Sort();
            dsl.add(sort);
        }
        return new SortBuilder(this, sort);
    }

    public Builder doc(Object doc) {
        if (this.doc == null) {
            this.doc = new Doc(doc);
            dsl.add(this.doc);
        }
        return this;
    }

    @Override
    public Esdsl build() {
        return dsl;
    }
}
