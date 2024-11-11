package io.github.s3s3l.yggdrasil.otel;

import java.net.URLConnection;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ContextExtractor {
    private final OpenTelemetry sdk;

    public Context extract(HttpServletRequest request) {
        return sdk.getPropagators()
                .getTextMapPropagator()
                .extract(Context.current(), request, TextMapGetters.HTTP_SERVLET_REQUEST_GETTER);
    }

    public Context extract(URLConnection conn) {
        return sdk.getPropagators()
                .getTextMapPropagator()
                .extract(Context.current(), conn, TextMapGetters.URL_CONNECTION_GETTER);
    }
}
