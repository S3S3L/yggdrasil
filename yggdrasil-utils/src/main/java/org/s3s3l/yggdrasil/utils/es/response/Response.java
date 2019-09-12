package org.s3s3l.yggdrasil.utils.es.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:Response <br>
 * Date: Jan 2, 2019 4:49:03 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
public class Response<T> {

    private Long took;
    @JsonProperty("timed_out")
    private Boolean timedOut;
    @JsonProperty("_shards")
    private Shards shards;
    private Hits<T> hits;

    @Data
    public static class Shards {
        private Integer total;
        private Integer successful;
        private Integer skipped;
        private Integer failed;
    }
}
