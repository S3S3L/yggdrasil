package io.github.s3s3l.yggdrasil.starter.datasource.config;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.github.s3s3l.yggdrasil.bean.time.JsonTimestampDateTimeDeserializer;
import io.github.s3s3l.yggdrasil.bean.time.JsonTimestampDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * </p>
 * ClassName:SwitchableDatasourceConfiguration <br>
 * Date: Nov 12, 2018 7:26:37 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SwitchableDatasourceConfiguration extends DatasourceConfiguration {

    /**
     * @since JDK 1.8
     */
    private static final long serialVersionUID = 3914384973570988277L;
    private Switch switchConf;

    @Data
    public static class Switch implements Serializable {
        /**
         * @since JDK 1.8
         */
        private static final long serialVersionUID = 1413195305565419929L;
        private DatasourceConfiguration db;
        @JsonDeserialize(using = JsonTimestampDateTimeDeserializer.class)
        @JsonSerialize(using = JsonTimestampDateTimeSerializer.class)
        private Timestamp time;
    }
}
