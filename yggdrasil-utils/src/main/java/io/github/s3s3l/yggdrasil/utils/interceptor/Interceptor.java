package io.github.s3s3l.yggdrasil.utils.interceptor;

import java.lang.reflect.Method;

import io.github.s3s3l.yggdrasil.utils.common.Named;

/**
 * <p>
 * </p>
 * ClassName:Interceptor <br>
 * Date: Aug 10, 2016 4:16:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Interceptor extends Named {

    /**
     * 
     * 
     * 
     * @param serialKey
     * @param proxy
     * @param method
     * @param params
     * @return nonull, return response directly; null, continue.
     * @throws Exception
     * @since JDK 1.8
     */
    Object before(String serialKey, Object proxy, Method method, Object... params);

    <T> T after(T response, String serialKey, Object proxy, Method method, Object... params);

    Object exception(Object response, String serialKey, Object proxy, Method method, Throwable e, Object... params)
            throws Throwable;

    void always(Object response, Object proxy, Method method, Object... params);
}
