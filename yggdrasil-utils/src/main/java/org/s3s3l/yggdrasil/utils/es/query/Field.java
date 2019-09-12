package org.s3s3l.yggdrasil.utils.es.query;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:Field <br>
 * Date: Dec 29, 2018 8:55:27 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
public class Field implements QueryBlock {
    private String name;
    private String value;

    public Field(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toQueryString() {
        return String.format("\"%s\":\"%s\"", name, value);
    }
}
