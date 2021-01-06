package org.s3s3l.yggdrasil.cache.key;

import org.s3s3l.yggdrasil.utils.common.ObjectSerializer;

/**
 * <p>
 * </p> 
 * ClassName:ComplexCacheKeyGenerator <br> 
 * Date:     Apr 26, 2018 3:03:13 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class SerializableCacheKeyGenerator implements CacheKeyGenerator<byte[]> {

    @Override
    public byte[] getKey(Object seed) {
        return ObjectSerializer.serialize(seed);
    }

}
  