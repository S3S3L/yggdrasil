package io.github.s3s3l.yggdrasil.sample.trace.es.query;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseQuery {
    private ZonedDateTime start;
    private ZonedDateTime end;
    private String index;
}
