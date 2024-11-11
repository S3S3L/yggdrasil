package io.github.s3s3l.yggdrasil.otel;

import java.net.URLConnection;
import java.util.Collections;

import io.opentelemetry.context.propagation.TextMapGetter;
import jakarta.servlet.http.HttpServletRequest;

public class TextMapGetters {
    public static final TextMapGetter<URLConnection> URL_CONNECTION_GETTER = new TextMapGetter<URLConnection>() {
        @Override
        public Iterable<String> keys(URLConnection carrier) {
            return carrier.getHeaderFields()
                          .keySet();
        }

        @Override
        public String get(URLConnection carrier, String key) {
            return carrier.getHeaderField(key);
        }
    };

    public static final TextMapGetter<HttpServletRequest> HTTP_SERVLET_REQUEST_GETTER = new TextMapGetter<HttpServletRequest>() {
        @Override
        public Iterable<String> keys(HttpServletRequest carrier) {
            return Collections.list(carrier.getHeaderNames());
        }

        @Override
        public String get(HttpServletRequest carrier, String key) {
            return carrier.getHeader(key);
        }
    };
    
}
