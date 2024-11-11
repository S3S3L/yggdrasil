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
            <#if body?has_content>
            ,
            {
                "match_phrase": {
                    "Body": "${body}"
                }
            }
            </#if>
        ]
    }
}