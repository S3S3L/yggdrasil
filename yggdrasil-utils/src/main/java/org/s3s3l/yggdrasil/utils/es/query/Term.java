package org.s3s3l.yggdrasil.utils.es.query;

/**
 * <p>
 * </p> 
 * ClassName:Term <br> 
 * Date:     Jan 2, 2019 9:47:54 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class Term implements QueryBlock {
    private Field field;
    
    public Term(Field field) {
        this.field = field;
    }

    @Override
    public String toQueryString() {
        return String.format("{\"term\": { %s } }", field.toQueryString());
    }

}
  