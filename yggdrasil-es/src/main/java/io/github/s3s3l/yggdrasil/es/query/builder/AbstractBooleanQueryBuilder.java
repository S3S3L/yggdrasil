package io.github.s3s3l.yggdrasil.es.query.builder;

import io.github.s3s3l.yggdrasil.es.query.BooleanQuery;

/**
 * <p>
 * </p>
 * ClassName:BooleanQueryBuilder <br>
 * Date: Jan 2, 2019 1:10:25 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class AbstractBooleanQueryBuilder
        implements BooleanQueryBlockBuilder, BooleanQuerySelector, BaseQueryBuilder {

    protected final EsdslBuilder queryBuilder;
    protected final BooleanQuery query;

    AbstractBooleanQueryBuilder(EsdslBuilder queryBuilder, BooleanQuery query) {
        this.queryBuilder = queryBuilder;
        this.query = query;
    }

    @Override
    public EsdslBuilder queryDone() {
        return queryBuilder;
    }

    @Override
    public AbstractBooleanQueryBuilder boolDone() {
        return this;
    }
    
    public AbstractBooleanQueryBuilder minimumShouldMatch(int minimumShouldMatch) {
        query.minimumShouldMatch(minimumShouldMatch);
        return this;
    }
}
