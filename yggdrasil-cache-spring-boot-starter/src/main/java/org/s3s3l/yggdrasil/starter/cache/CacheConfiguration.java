package org.s3s3l.yggdrasil.starter.cache;

import java.util.function.Supplier;

import org.s3s3l.yggdrasil.annotation.apollo.ApolloConfiguration;
import org.s3s3l.yggdrasil.utils.cache.compressor.CompressProperties;
import org.s3s3l.yggdrasil.utils.compressor.Compressor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * </p>
 * ClassName:CacheConfiguration <br>
 * Date: Apr 10, 2019 7:58:40 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@ToString
@Data
@ApolloConfiguration
@ConditionalOnMissingBean(CacheConfiguration.class)
@ConfigurationProperties(prefix = CacheConfiguration.PREFIX)
public class CacheConfiguration {
    public static final String PREFIX = "yggdrasil.cache";

    private boolean enable;
    private String dataRedis;
    private String versionRedis;

    private int remoteExpireAfterWrite = -1;
    private int remoteExpireAfterAccess = -1;
    private int localExpireAfterAccess = -1;
    private int localExpireAfterWrite = -1;
    private int localMaxNum = 500;
    private String keyPrefix = "c";
    private CompressProperties compress = new CompressProperties();

    private Class<Supplier<Compressor>> compressorSupplier;

}
