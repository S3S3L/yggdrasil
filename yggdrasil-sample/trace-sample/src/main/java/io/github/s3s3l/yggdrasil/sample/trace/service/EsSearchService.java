package io.github.s3s3l.yggdrasil.sample.trace.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import io.github.s3s3l.yggdrasil.otel.data.es.log.LogData;
import io.github.s3s3l.yggdrasil.otel.data.es.trace.TraceData;
import io.github.s3s3l.yggdrasil.otel.data.es.trace.TraceTreeNode;
import io.github.s3s3l.yggdrasil.sample.trace.configuration.EsClientConfig;
import io.github.s3s3l.yggdrasil.sample.trace.es.EsClientHelper;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.LogQuery;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.SearchQuery;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.TraceQuery;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

@Service
public class EsSearchService {

    @Autowired
    EsClientHelper esClientHelper;

    @Autowired
    EsClientConfig esClientConfig;

    public List<LogData> searchLogs(LogQuery query) {
        return esClientHelper.doSearch(query, LogData.class);
    }

    public List<TraceData> searchTraces(TraceQuery query) {

        return esClientHelper.doSearch(query, TraceData.class);
    }

    public SortedSet<TraceTreeNode> searchTracingTree(SearchQuery query) {
        SortedSet<TraceTreeNode> result = new TreeSet<>();

        List<StringTermsBucket> searchResult = esClientHelper.doTerms(query, "TraceId.keyword");

        if (CollectionUtils.isEmpty(searchResult)) {
            return result;
        }

        Set<String> traceids = searchResult.stream()
                .map(b -> b.key()
                        .stringValue())
                .collect(Collectors.toSet());

        SearchQuery traceQuery = new SearchQuery();
        traceQuery.from(query);
        traceQuery.setIndex(esClientConfig.getTraceIndex());
        traceQuery.setTraceIds(traceids);
        List<TraceData> traces = esClientHelper.doSearch(traceQuery, TraceData.class);

        SearchQuery logQuery = new SearchQuery();
        logQuery.from(query);
        logQuery.setIndex(esClientConfig.getLogIndex());
        logQuery.setTraceIds(traceids);
        List<LogData> logs = esClientHelper.doSearch(logQuery, LogData.class);

        Map<String, TraceTreeNode> traceTreeNodes = traces.stream()
                .collect(Collectors.toMap(TraceData::getSpanId, t -> TraceTreeNode.builder()
                        .root(StringUtils.isEmpty(t.getParentSpanId()))
                        .data(t)
                        .build(), (a, b) -> a));

        for (LogData logData : logs) {
            if (StringUtils.isEmpty(logData.getSpanId())) {
                continue;
            }

            traceTreeNodes.computeIfPresent(logData.getSpanId(), (spanId, node) -> {
                node.addChild(TraceTreeNode.builder()
                        .data(logData)
                        .build());
                return node;
            });
        }

        for (TraceTreeNode node : traceTreeNodes.values()) {
            if (node.isRoot()) {
                result.add(node);
                continue;
            }

            traceTreeNodes.computeIfPresent(node.getData()
                    .getParentSpanId(), (spanId, parentNode) -> {
                        parentNode.addChild(node);
                        return parentNode;
                    });
        }

        return result;
    }
}
