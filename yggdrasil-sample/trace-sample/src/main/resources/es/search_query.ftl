{
    "bool": {
        "must": [],
        "must_not": [
            {
                "match_phrase": {
                    "TraceFlags": "0"
                }
            }
        ],
        "filter": [
            {
                "range": {
                    "@timestamp": {
                        "format": "strict_date_optional_time",
                        "gte": "${start}",
                        "lte": "${end}"
                    }
                }
            }
            <#if keyWords?has_content>
            ,
            {
                "multi_match": {
                    "type": "best_fields",
                    "query": "${keyWords}",
                    "lenient": true
                }
            }
            </#if>
            <#if keyWords?has_content>
            ,
            {
                "bool": {
                    "minimum_should_match": 1,
                    "should": [
                        <#list traceIds as traceId>
                            {
                                "match_phrase": {
                                    "TraceId.keyword": "${traceId}"
                                }
                            }
                            <#if traceId?has_next>,</#if>
                        </#list>
                    ]
                }
            }
            </#if>
        ]
    }
}