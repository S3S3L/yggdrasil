package org.s3s3l.yggdrasil.starter.apollo;

import java.util.List;

import org.s3s3l.yggdrasil.utils.apollo.ConfigFileChangedProcessor;
import org.s3s3l.yggdrasil.utils.file.FileFormat;
import org.s3s3l.yggdrasil.utils.spring.env.LocationType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;

import lombok.Data;

/**
 * 
 * <p>
 * </p>
 * ClassName: ApolloConfiguration <br>
 * date: Jan 14, 2019 3:37:39 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
@ConfigurationProperties(prefix = ApolloConfiguration.PREFIX)
public class ApolloConfiguration {
    public static final String PREFIX = "yggdrasil.apollo";

    private boolean enable;
    private String meta;
    private String appId;
    private List<Document> docs;

    @Data
    public static class Document implements Comparable<Document> {
        private String name;
        private int priority;
        private ConfigFileFormat format;
        private Location location;
        private String target;
        private Class<? extends ConfigFileChangedProcessor>[] processors;
        private List<FieldDoc> fieldDoc;
        
        @Override
        public int compareTo(Document o) {
            return this.priority - o.getPriority();
        }
    }

    @Data
    public static class FieldDoc {
        private String key;
        private String defaultKey;
        private FileFormat format;
    }

    @Data
    public static class Location {
        private LocationType type;
        private String target;
    }
}
