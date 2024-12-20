package io.github.s3s3l.yggdrasil.sample.trace.es.query;

import io.github.s3s3l.yggdrasil.template.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Template("log_query")
public class LogQuery extends BaseQuery {
    private String body;
}
