
package io.github.s3s3l.yggdrasil.cache.key;

import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;

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

    private final JacksonHelper jackson;

    public JacksonCacheKeyGenerator(JacksonHelper jackson) {
        this.jackson = jackson;
    }

    @Override
    public byte[] getKey(Object seed) {
        return jackson.toStructuralBytes(seed);
    }

}
