package io.github.s3s3l.yggdrasil.es.query.builder;

import io.github.s3s3l.yggdrasil.es.query.BooleanQuery;
import io.github.s3s3l.yggdrasil.es.query.BooleanQueryBlock;
import io.github.s3s3l.yggdrasil.es.query.Range.RangeQueryBuilder;

/**
 * <p>
 * </p>
 * ClassName:DefaultBooleanQueryBuilder <br>
 * Date: Jan 2, 2019 1:35:42 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class BooleanQueryBuilder extends AbstractBooleanQueryBuilder {
    private BooleanQueryBlock queryBlock;

    BooleanQueryBuilder(EsdslBuilder queryBuilder, BooleanQuery query) {
        super(queryBuilder, query);
    }

    @Override
    public BooleanQueryBlockBuilder match(String name, String value) {
        queryBlock.match(name, value);
        return this;
    }

    @Override
    public BooleanQueryBlockBuilder term(String name, String value) {
        queryBlock.term(name, value);
        return this;
    }

    @Override
    public RangeFieldCreator<BooleanQueryBlockBuilder> range() {
        return new RangeQueryBuilder<>(this, queryBlock);
    }

    @Override
    public BooleanQueryBlockBuilder should() {
        queryBlock = query.should();
        return this;
    }

    @Override
    public BooleanQueryBlockBuilder must() {
        queryBlock = query.must();
        return this;
    }

    @Override
    public BooleanQueryBlockBuilder filter() {
        queryBlock = query.filter();
        return this;
    }

}
