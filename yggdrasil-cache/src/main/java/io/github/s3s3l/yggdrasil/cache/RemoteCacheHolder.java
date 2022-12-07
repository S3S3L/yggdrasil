
package io.github.s3s3l.yggdrasil.cache;

/**
 * <p>
 * </p>
 * ClassName:RemoteCacheHolder <br>
 * Date: Apr 8, 2019 5:52:34 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface RemoteCacheHolder<K, V> extends ScopeCacheHolder<K, V> {
    
    RemoteCacheHolder<K, V> expireAfterAccess(int seconds);
    
    RemoteCacheHolder<K, V> expireAfterWrite(int seconds);
}
