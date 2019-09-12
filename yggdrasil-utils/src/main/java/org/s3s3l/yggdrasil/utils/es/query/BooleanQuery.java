package org.s3s3l.yggdrasil.utils.es.query;

import java.util.HashMap;
import java.util.Map;

import org.s3s3l.yggdrasil.utils.common.StringUtils;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:Query <br>
 * Date: Jan 2, 2019 11:33:32 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
public class BooleanQuery implements QueryBlock {
    private static final String SPLITOR = ",";
    private Map<Class<? extends BooleanQueryBlock>, BooleanQueryBlock> bools = new HashMap<>();
    private Map<String, Field> properties = new HashMap<>();

    @Override
    public String toQueryString() {
        String queryString = StringUtils.EMPTY_STRING;
        for (Field property : properties.values()) {
            queryString = String.join(SPLITOR, queryString, property.toQueryString());
        }
        for (QueryBlock bool : bools.values()) {
            queryString = String.join(SPLITOR, queryString, bool.toQueryString());
        }
        if (queryString.startsWith(SPLITOR)) {
            queryString = queryString.replaceFirst(SPLITOR, StringUtils.EMPTY_STRING);
        }

        return String.format("{\"bool\": {%s}}", queryString);
    }

    public BooleanQueryBlock should() {
        BooleanQueryBlock should = this.bools.get(Should.class);
        if (should == null) {
            should = new Should();
            this.bools.put(Should.class, should);
        }
        return should;
    }

    public BooleanQueryBlock must() {
        BooleanQueryBlock must = this.bools.get(Must.class);
        if (must == null) {
            must = new Must();
            this.bools.put(Must.class, must);
        }
        return must;
    }

    public BooleanQueryBlock filter() {
        BooleanQueryBlock filter = this.bools.get(Filter.class);
        if (filter == null) {
            filter = new Filter();
            this.bools.put(Filter.class, filter);
        }
        return filter;
    }

    public BooleanQuery minimumShouldMatch(int minimumShouldMatch) {
        final String name = "minimum_should_match";
        this.properties.put(name, new Field(name, String.valueOf(minimumShouldMatch)));
        return this;
    }

}
