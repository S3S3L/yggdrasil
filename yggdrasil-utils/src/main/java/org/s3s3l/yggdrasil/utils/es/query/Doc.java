package org.s3s3l.yggdrasil.utils.es.query;

import java.util.List;

import org.s3s3l.yggdrasil.utils.json.JacksonUtils;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:Doc <br>
 * Date: Jan 7, 2019 11:14:19 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
public class Doc implements QueryBlock {
    private Object doc;
    private List<Field> fields;

    public Doc(Object doc) {
        this.doc = doc;
    }

    @Override
    public String toQueryString() {
        return String.format("\"doc\": %s", JacksonUtils.nonNull.toJsonString(doc));
    }

}
