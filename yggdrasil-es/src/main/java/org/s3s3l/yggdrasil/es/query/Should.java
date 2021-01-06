package org.s3s3l.yggdrasil.es.query;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:Should <br>
 * Date: Dec 29, 2018 8:59:32 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
public class Should implements BooleanQueryBlock {
    private List<QueryBlock> querys = new LinkedList<>();

    @Override
    public String toQueryString() {
        return String.format("\"should\": [%s]", String.join(",", querys.stream()
                .map(QueryBlock::toQueryString)
                .collect(Collectors.toList())));
    }

    @Override
    public BooleanQueryBlock conditions(QueryBlock... querys) {
        for (QueryBlock query : querys) {
            this.querys.add(query);
        }
        return this;
    }

    @Override
    public BooleanQueryBlock conditions(List<QueryBlock> querys) {
        this.querys.addAll(querys);
        return this;
    }

    @Override
    public BooleanQueryBlock match(String name, String value) {
        this.querys.add(new Match(new Field(name, value)));
        return this;
    }

    @Override
    public BooleanQueryBlock term(String name, String value) {
        this.querys.add(new Term(new Field(name, value)));
        return this;
    }

    @Override
    public void setRange(Range range) {
        this.querys.add(range);
    }

    @Override
    public BooleanQueryBlock range(Range range) {
        setRange(range);
        return this;
    }

}
