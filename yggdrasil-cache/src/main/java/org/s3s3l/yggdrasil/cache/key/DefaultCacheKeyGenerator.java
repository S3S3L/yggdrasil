package org.s3s3l.yggdrasil.cache.key;

import java.nio.charset.StandardCharsets;

import org.s3s3l.yggdrasil.utils.common.ObjectSerializer;

/**
 * <p>
 * </p>
 * ClassName:DefaultCacheKeyGenerator <br>
 * Date: Sep 25, 2017 6:32:23 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultCacheKeyGenerator implements CacheKeyGenerator<String> {

    @Override
    public String getKey(Object seed) {
        return new String(ObjectSerializer.serialize(seed), StandardCharsets.UTF_8);
    }

}
