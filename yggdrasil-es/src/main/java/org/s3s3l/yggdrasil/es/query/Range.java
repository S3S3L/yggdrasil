package org.s3s3l.yggdrasil.es.query;

import java.util.HashMap;
import java.util.Map;

import org.s3s3l.yggdrasil.es.query.builder.RangeFieldBuilder;
import org.s3s3l.yggdrasil.es.query.builder.RangeFieldCreator;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * <p>
 * </p>
 * ClassName:Range <br>
 * Date: Jan 3, 2019 11:47:09 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class Range implements QueryBlock {

    private Map<String, RangeField> fields = new HashMap<>();

    @Override
    public String toQueryString() {
        return String.format("{\"range\": %s}", JacksonUtils.nonNull.toStructuralString(fields));
    }

    @Getter
    public static class RangeField {
        private String gte;
        private String gt;
        private String lte;
        private String lt;
        private String format;
        @JsonProperty("time_zone")
        private String timeZone;
    }

    public static class RangeQueryBuilder<T> implements RangeFieldCreator<T>, RangeFieldBuilder<T> {
        private final T preBuilder;
        private final RangableQueryBlock query;
        private Range range = new Range();
        private RangeField currentField;
        private String currentName;

        public RangeQueryBuilder(T preBuilder, RangableQueryBlock query) {
            this.preBuilder = preBuilder;
            this.query = query;
        }

        private RangeField currentField() {
            if (currentField == null) {
                throw new RuntimeException("Call field first.");
            }

            return currentField;
        }

        @Override
        public RangeFieldBuilder<T> field(String name) {
            currentField = new RangeField();
            currentName = name;
            return this;
        }

        @Override
        public RangeQueryBuilder<T> gte(String gte) {
            currentField().gte = gte;
            return this;
        }

        @Override
        public RangeQueryBuilder<T> gt(String gt) {
            currentField().gt = gt;
            return this;
        }

        @Override
        public RangeQueryBuilder<T> lte(String lte) {
            currentField().lte = lte;
            return this;
        }

        @Override
        public RangeQueryBuilder<T> lt(String lt) {
            currentField().lt = lt;
            return this;
        }

        @Override
        public RangeQueryBuilder<T> format(String format) {
            currentField().format = format;
            return this;
        }

        @Override
        public RangeQueryBuilder<T> timeZone(String timeZone) {
            currentField().timeZone = timeZone;
            return this;
        }

        @Override
        public RangeFieldCreator<T> fieldDone() {
            range.fields.put(currentName, currentField);
            return this;
        }

        @Override
        public T rangeDone() {
            query.setRange(range);
            return this.preBuilder;
        }
    }

}
