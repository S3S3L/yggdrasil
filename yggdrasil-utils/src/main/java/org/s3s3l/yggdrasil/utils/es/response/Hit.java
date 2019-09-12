package org.s3s3l.yggdrasil.utils.es.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <p>
 * </p> 
 * ClassName:Hit <br> 
 * Date:     Jan 2, 2019 5:01:20 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
@Data
public class Hit<T> {

    @JsonProperty("_index")
    private String index;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_score")
    private String score;
    @JsonProperty("_source")
    private T source;
}
  