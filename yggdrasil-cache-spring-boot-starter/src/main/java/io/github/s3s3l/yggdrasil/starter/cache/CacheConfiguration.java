package io.github.s3s3l.yggdrasil.starter.cache;

import java.util.function.Supplier;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import io.github.s3s3l.yggdrasil.annotation.apollo.ApolloConfiguration;
import io.github.s3s3l.yggdrasil.cache.compressor.CompressProperties;
import io.github.s3s3l.yggdrasil.compress.Compressor;
import io.github.s3s3l.yggdrasil.compress.DefaultZstdCompressSupliers;
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
    private boolean web = false;

    private int remoteExpireAfterWrite = -1;
    private int remoteExpireAfterAccess = -1;
    private int localExpireAfterAccess = -1;
    private int localExpireAfterWrite = -1;
    private int localMaxNum = 500;
    private String keyPrefix = "c";
    private CompressProperties compress = new CompressProperties();

    private Class<? extends Supplier<Compressor>> compressorSupplier = DefaultZstdCompressSupliers.class;

}
