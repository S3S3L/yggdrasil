package io.github.s3s3l.yggdrasil.sample.trace;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CrosFilter implements Filter {
    @Autowired
    OpenTelemetry openTelemetry;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        ContentCachingResponseWrapper response = new ContentCachingResponseWrapper((HttpServletResponse) res);
        HttpServletRequest request = new HttpServletRequestWrapper((HttpServletRequest) req);

        Tracer tracer = openTelemetry.getTracer(RollController.class.getName(), "0.1.0");
        Span span = tracer.spanBuilder("request").startSpan();

        log.info("request log out span. path: {}", request.getRequestURI());

        try (Scope scope = span.makeCurrent()) {

            log.info("request log in span. path: {}", request.getRequestURI());

            span.setAttribute("path", request.getRequestURI());
            span.getSpanContext().getTraceId();

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
            response.setHeader("Access-Control-Allow-Method", "*");

            chain.doFilter(request, response);
            
            span.setAttribute("response size", response.getContentSize());

            response.copyBodyToResponse();
        } finally {
            span.end();
        }
    }

}
