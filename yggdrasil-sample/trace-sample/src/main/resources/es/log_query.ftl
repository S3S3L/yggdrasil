{
    "bool": {
        "must": [],
        "filter": [
            {
                "range": {
                    "@timestamp": {
                        "format": "strict_date_optional_time",
                        "gte": "${start}",
                        "lte": "${end}"
                    }
                }
            },
            {
                "match_phrase": {
                    "Body": "${body}"
                }
            }
        ],
        "should": [],
        "must_not": []
    }
}