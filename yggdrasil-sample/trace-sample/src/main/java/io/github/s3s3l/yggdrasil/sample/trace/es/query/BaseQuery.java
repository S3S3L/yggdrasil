package io.github.s3s3l.yggdrasil.sample.trace.es.query;

import java.time.ZonedDateTime;

import io.github.s3s3l.yggdrasil.bean.verify.Examine;
import io.github.s3s3l.yggdrasil.bean.verify.Expectation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseQuery {
    @Examine(value = Expectation.NOT_NULL)
    private ZonedDateTime start;
    @Examine(value = Expectation.NOT_NULL)
    private ZonedDateTime end;
    @Examine(value = Expectation.HAS_LENGTH)
    private String index;
    @Builder.Default
    private int from = 0;
    @Builder.Default
    private int size = 500;

    public void from(BaseQuery query) {
        this.start = query.start;
        this.end = query.end;
        this.index = query.index;
        this.from = query.from;
        this.size = query.size;
    }
}
