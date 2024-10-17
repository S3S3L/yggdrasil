package io.github.s3s3l.yggdrasil.sample.trace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.s3s3l.yggdrasil.otel.data.es.trace.LogData;
import io.github.s3s3l.yggdrasil.otel.data.es.trace.TraceData;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.LogQuery;
import io.github.s3s3l.yggdrasil.sample.trace.es.query.TraceQuery;
import io.github.s3s3l.yggdrasil.sample.trace.service.EsSearchService;


@RequestMapping("otel/search")
@RestController
public class OtelSearchController {

    @Autowired
    EsSearchService esSearchService;
    
    @GetMapping("log")
    public List<LogData> log(@ModelAttribute LogQuery query) {
        query.setIndex("otel_log_index");
        return esSearchService.searchLogs(query);
    }

    @GetMapping("trace")
    public List<TraceData> trace(@ModelAttribute TraceQuery query) {
        query.setIndex("otel_trace_index");
        return esSearchService.searchTraces(query);
    }
    
}
