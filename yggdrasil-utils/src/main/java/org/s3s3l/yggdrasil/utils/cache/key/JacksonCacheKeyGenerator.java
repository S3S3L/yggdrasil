
package org.s3s3l.yggdrasil.utils.cache.key;

import org.s3s3l.yggdrasil.utils.json.IJacksonHelper;

/**
 * <p>
 * </p>
 * ClassName:YamlCacheKeyGenerator <br>
 * Date: Apr 9, 2019 4:00:59 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class JacksonCacheKeyGenerator implements CacheKeyGenerator<byte[]> {

    private final IJacksonHelper jackson;

    public JacksonCacheKeyGenerator(IJacksonHelper jackson) {
        this.jackson = jackson;
    }

    @Override
    public byte[] getKey(Object seed) {
        return jackson.toJsonBytes(seed);
    }

}
