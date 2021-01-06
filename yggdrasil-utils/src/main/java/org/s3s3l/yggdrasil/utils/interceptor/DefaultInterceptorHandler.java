package org.s3s3l.yggdrasil.utils.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.s3s3l.yggdrasil.utils.common.StringUtils;

/**
 * ClassName:LogInterceptor <br>
 * Date: 2016年4月25日 下午1:44:17 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultInterceptorHandler implements InterceptorHandler {
    private Object target;
    private Set<Interceptor> interceptorChain = new LinkedHashSet<>();

    @Override
    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public void addInterceptor(Collection<Interceptor> interceptors) {
        this.interceptorChain.addAll(interceptors);
    }

    @Override
    public void addInterceptor(Interceptor interceptor) {
        this.interceptorChain.add(interceptor);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            for (Interceptor interceptor : interceptorChain) {
                result = interceptor.before(StringUtils.EMPTY_STRING, proxy, method, args);
                if (result != null) {
                    return result;
                }
            }
            result = method.invoke(target, args);
            for (Interceptor interceptor : interceptorChain) {
                result = interceptor.after(result, StringUtils.EMPTY_STRING, proxy, method, args);
            }
        } catch (InvocationTargetException e) {
            for (Interceptor interceptor : interceptorChain) {
                result = interceptor.exception(result, StringUtils.EMPTY_STRING, proxy, method, e.getTargetException(),
                        args);
            }
        } finally {
            for (Interceptor interceptor : interceptorChain) {
                interceptor.always(result, proxy, method, args);
            }
        }
        return result;
    }

}
