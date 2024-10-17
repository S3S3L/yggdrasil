package io.github.s3s3l.yggdrasil.sample.trace.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.s3s3l.yggdrasil.otel.data.es.trace.LogData;
import io.github.s3s3l.yggdrasil.otel.data.es.trace.TraceData;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.LogQuery;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.TraceQuery;
import io.github.s3s3l.yggdrasil.template.TemplateManager;

@Service
public class EsSearchService {

    @Autowired
    ElasticsearchClient esClient;

    @Autowired
    TemplateManager templateManager;

    public List<LogData> searchLogs(LogQuery query) {
        String queryStr = templateManager.compile(query, LogQuery.class);

        String base64Query = Base64.getEncoder().encodeToString(queryStr.getBytes());
        try {
            return esClient.search(builder -> builder.index(query.getIndex())
                    .query(QueryBuilders.wrapper(w -> w.query(base64Query))),
                    LogData.class).hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TraceData> searchTraces(TraceQuery query) {
        String queryStr = templateManager.compile(query, TraceQuery.class);

        String base64Query = Base64.getEncoder().encodeToString(queryStr.getBytes());
        try {
            return esClient.search(builder -> builder.index(query.getIndex())
                    .query(QueryBuilders.wrapper(w -> w.query(base64Query))),
                    TraceData.class).hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (ElasticsearchException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
