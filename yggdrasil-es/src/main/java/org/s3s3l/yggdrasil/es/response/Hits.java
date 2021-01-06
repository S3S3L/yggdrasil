package org.s3s3l.yggdrasil.es.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <p>
 * </p> 
 * ClassName:Hits <br> 
 * Date:     Jan 2, 2019 5:03:01 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
@Data
public class Hits<T> {

    private long total;
    @JsonProperty("max_score")
    private double maxScore;
    private List<Hit<T>> hits;
}
  