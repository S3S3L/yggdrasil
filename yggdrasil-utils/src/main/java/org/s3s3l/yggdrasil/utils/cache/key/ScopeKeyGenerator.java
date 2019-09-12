package org.s3s3l.yggdrasil.utils.cache.key;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * <p>
 * </p>
 * ClassName:ScopeKeyGenerator <br>
 * Date: Apr 26, 2018 11:38:37 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class ScopeKeyGenerator {

    public static final Function<String, byte[]> redisKeyGenerator = scope -> new StringBuilder("CACHE:").append(scope)
            .toString()
            .getBytes(StandardCharsets.UTF_8);
}
