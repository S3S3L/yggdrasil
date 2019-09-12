package org.s3s3l.yggdrasil.utils.es.query;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

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
public class Must implements BooleanQueryBlock {
    private List<QueryBlock> querys = new LinkedList<>();

    @Override
    public String toQueryString() {
        return String.format("\"must\": [%s]", String.join(",", querys.stream()
                .map(r -> r.toQueryString())
                .collect(Collectors.toList())));
    }

    @Override
    public BooleanQueryBlock conditions(@Nonnull QueryBlock... querys) {
        for (QueryBlock query : querys) {
            this.querys.add(query);
        }
        return this;
    }

    @Override
    public BooleanQueryBlock conditions(@Nonnull List<QueryBlock> querys) {
        this.querys.addAll(querys);
        return this;
    }

    @Override
    public BooleanQueryBlock match(@Nonnull String name, @Nonnull String value) {
        this.querys.add(new Match(new Field(name, value)));
        return this;
    }

    @Override
    public BooleanQueryBlock term(String name, String value) {
        this.querys.add(new Term(new Field(name, value)));
        return this;
    }

    @Override
    public BooleanQueryBlock range(Range range) {
        setRange(range);
        return this;
    }

    @Override
    public void setRange(Range range) {
        this.querys.add(range);
    }

}
