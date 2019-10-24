package org.s3s3l.yggdrasil.es.query;

import lombok.Data;

/**
 * <p>
 * </p> 
 * ClassName:MixedQuery <br> 
 * Date:     Jan 2, 2019 1:52:13 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
@Data
public class QueryBody implements QueryBlock {

    private BooleanQuery bool;
    
    public QueryBody(BooleanQuery bool) {
        this.bool = bool;
    }

    @Override
    public String toQueryString() {
        return String.format("\"query\": %s", bool.toQueryString());
    }
}
  