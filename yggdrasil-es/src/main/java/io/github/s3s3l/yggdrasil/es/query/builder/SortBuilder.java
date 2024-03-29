package io.github.s3s3l.yggdrasil.es.query.builder;

import io.github.s3s3l.yggdrasil.es.enumerations.OrderType;
import io.github.s3s3l.yggdrasil.es.enumerations.SortMode;
import io.github.s3s3l.yggdrasil.es.query.Sort;
import io.github.s3s3l.yggdrasil.es.query.Sort.SortProperty;

/**
 * <p>
 * </p>
 * ClassName:AbstractSortBuilder <br>
 * Date: Jan 7, 2019 12:25:03 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class SortBuilder implements SortSelector, SortPropertyBuilder {
    private final Sort sort;
    private final EsdslBuilder preBuilder;
    private SortProperty currentProperty;

    public SortBuilder(EsdslBuilder builder, Sort sort) {
        this.sort = sort;
        this.preBuilder = builder;
    }

    @Override
    public SortPropertyBuilder order(OrderType orderType) {
        currentProperty.setOrder(orderType);
        return this;
    }

    @Override
    public SortPropertyBuilder mode(SortMode sortMode) {
        currentProperty.setMode(sortMode);
        return this;
    }

    @Override
    public SortPropertyBuilder missing(String missing) {
        currentProperty.setMissing(missing);
        return this;
    }

    @Override
    public SortPropertyBuilder field(String field) {
        currentProperty = new SortProperty();
        sort.getSorts()
                .put(field, currentProperty);
        return this;
    }

    @Override
    public EsdslBuilder sortDone() {
        return preBuilder;
    }

    @Override
    public SortSelector propertyDone() {
        return this;
    }

}
