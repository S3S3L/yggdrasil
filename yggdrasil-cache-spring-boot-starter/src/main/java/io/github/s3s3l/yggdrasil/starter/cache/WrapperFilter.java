package io.github.s3s3l.yggdrasil.starter.cache;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

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
