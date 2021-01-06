
package org.s3s3l.yggdrasil.redis.base;

/**
 * <p>
 * </p>
 * ClassName:InitializableRedis <br>
 * Date: Sep 20, 2018 12:09:51 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface InitializableRedis<T> extends IRedis {

    void init(T configuration);
}
