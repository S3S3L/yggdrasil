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
@Template("trace_query")
public class TraceQuery extends BaseQuery {
    private String path;
}
