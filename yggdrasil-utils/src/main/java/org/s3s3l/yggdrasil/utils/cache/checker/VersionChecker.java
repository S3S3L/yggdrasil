package org.s3s3l.yggdrasil.utils.cache.checker;

/**
 * <p>
 * </p>
 * ClassName:VersionChecker <br>
 * Date: Apr 4, 2019 1:22:01 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface VersionChecker {

    /**
     * 
     * check if a scope is fresh, if not local cache need to be refresh.
     * 
     * @param scope
     * @return
     * @since JDK 1.8
     */
    boolean isFresh(String scope);

    /**
     * 
     * 获取本地版本号
     * 
     * @param scope
     * @return
     * @since JDK 1.8
     */
    Long localVersion(String scope);

}
