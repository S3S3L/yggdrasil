package io.github.s3s3l.yggdrasil.cache.checker;

/**
 * <p>
 * </p>
 * ClassName:CacheChecker <br>
 * Date: Sep 21, 2017 1:18:28 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface CacheChecker extends VersionChecker {

    /**
     * 
     * 同步远端版本到本地
     * 
     * @param scope 
     * @since JDK 1.8
     */
    void syncRemote(String scope);
    
    /**
     * 
     * 升级本地版本并同步本地版本到远端
     * 
     * @param scope 
     * @since JDK 1.8
     */
    void syncLocal(String scope);
}
