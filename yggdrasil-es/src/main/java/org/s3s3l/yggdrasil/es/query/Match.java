package org.s3s3l.yggdrasil.es.query;

/**
 * <p>
 * </p>
 * ClassName:Match <br>
 * Date: Dec 29, 2018 8:49:28 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class Match implements QueryBlock {
    private Field field;

    public Match(Field field) {
        this.field = field;
    }

    @Override
    public String toQueryString() {
        return String.format("{\"match\": { %s } }", field.toQueryString());
    }

}
