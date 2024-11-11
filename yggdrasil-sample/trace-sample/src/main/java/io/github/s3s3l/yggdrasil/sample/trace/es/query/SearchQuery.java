package io.github.s3s3l.yggdrasil.sample.trace.es.query;

import java.util.Set;

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
@Template("search_query")
public class SearchQuery extends BaseQuery {
    private Set<String> traceIds;
    private String keyWords;
}
