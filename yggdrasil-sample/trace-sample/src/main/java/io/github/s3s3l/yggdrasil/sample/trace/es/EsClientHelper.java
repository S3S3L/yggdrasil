package io.github.s3s3l.yggdrasil.sample.trace.es;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.node.ObjectNode;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.BaseQuery;
import io.github.s3s3l.yggdrasil.template.TemplateManager;

public class EsClientHelper {

    private final ElasticsearchClient esClient;
    private final TemplateManager templateManager;

    public EsClientHelper(ElasticsearchClient esClient, TemplateManager templateManager) {
        this.esClient = esClient;
        this.templateManager = templateManager;
    }

    public <C extends BaseQuery, R> List<R> doSearch(C query, Class<R> resultType) {
        String queryStr = templateManager.compile(query);

        String base64Query = Base64.getEncoder()
                .encodeToString(queryStr.getBytes(StandardCharsets.UTF_8));

        try {
            SearchResponse<R> searchResponse = esClient.search(builder -> builder.index(query.getIndex())
                    .query(QueryBuilders.wrapper(w -> w.query(base64Query)))
                    .from(query.getFrom())
                    .size(query.getSize()), resultType);
            return searchResponse.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <C extends BaseQuery> List<StringTermsBucket> doTerms(C query, String field) {
        String queryStr = templateManager.compile(query);

        String base64Query = Base64.getEncoder()
                .encodeToString(queryStr.getBytes(StandardCharsets.UTF_8));

        try {
            SearchResponse<ObjectNode> searchResponse = esClient.search(builder -> builder.index(query.getIndex())
                    .query(QueryBuilders.wrapper(w -> w.query(base64Query)))
                    .aggregations("terms", aggs -> aggs.terms(t -> t.field(field)
                            .size(query.getSize())))
                    .from(query.getFrom())
                    .size(0), ObjectNode.class);
            return searchResponse.aggregations()
                    .get("terms")
                    .sterms()
                    .buckets()
                    .array();
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
