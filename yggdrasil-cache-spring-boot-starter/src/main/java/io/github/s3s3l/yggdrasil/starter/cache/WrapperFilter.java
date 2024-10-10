package io.github.s3s3l.yggdrasil.starter.cache;

import java.io.IOException;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class WrapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        ServletResponse res = response;
        ServletRequest req = request;
        if (request instanceof HttpServletRequest && !(request instanceof ContentCachingRequestWrapper)) {
            req = new ContentCachingRequestWrapper((HttpServletRequest) request);
        }
        if (response instanceof HttpServletResponse && !(response instanceof ContentCachingResponseWrapper)) {
            res = new ContentCachingResponseWrapper((HttpServletResponse) response);
        }
        chain.doFilter(req, res);

        if (res instanceof ContentCachingResponseWrapper) {
            ((ContentCachingResponseWrapper) res).copyBodyToResponse();
        }
    }

    @Override
    public void destroy() {
    }

}
