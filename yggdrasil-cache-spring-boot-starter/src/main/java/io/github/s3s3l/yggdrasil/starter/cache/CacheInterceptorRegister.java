package io.github.s3s3l.yggdrasil.starter.cache;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CacheInterceptorRegister implements WebMvcConfigurer {

    private final HandlerInterceptor[] interceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (HandlerInterceptor interceptor : interceptors) {
            registry.addInterceptor(interceptor).addPathPatterns("/**");
        }
    }
    
}
