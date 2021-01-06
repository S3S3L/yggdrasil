package org.s3s3l.yggdrasil.utils.interceptor;

import java.lang.reflect.InvocationHandler;
import java.util.Collection;

/**
 * ClassName:InterceptorHandler <br>
 * Date: 2016年4月25日 下午4:12:18 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface InterceptorHandler extends InvocationHandler {

    void setTarget(Object target);

    void addInterceptor(Collection<Interceptor> interceptors);

    void addInterceptor(Interceptor interceptor);
}
