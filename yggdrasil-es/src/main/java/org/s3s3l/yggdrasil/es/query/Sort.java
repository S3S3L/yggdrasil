package org.s3s3l.yggdrasil.es.query;

import java.util.HashMap;
import java.util.Map;

import org.s3s3l.yggdrasil.es.enumerations.OrderType;
import org.s3s3l.yggdrasil.es.enumerations.SortMode;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:Sort <br>
 * Date: Jan 6, 2019 11:27:17 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
public class Sort implements QueryBlock {
    private Map<String, SortProperty> sorts = new HashMap<>();

    @Override
    public String toQueryString() {
        return String.format("\"sort\": %s", JacksonUtils.nonNull.toStructuralString(sorts));
    }

    @Data
    public static class SortProperty {
        private OrderType order;
        private SortMode mode;
        private String missing;
    }
}
