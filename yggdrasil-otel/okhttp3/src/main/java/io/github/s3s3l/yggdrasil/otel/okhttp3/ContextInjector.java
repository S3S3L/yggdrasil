package io.github.s3s3l.yggdrasil.otel.okhttp3;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import lombok.AllArgsConstructor;
import okhttp3.Request;

@AllArgsConstructor
public class ContextInjector {
    private final OpenTelemetry sdk;

    public void inject(Request.Builder requestBuilder) {
        sdk.getPropagators()
           .getTextMapPropagator()
           .inject(Context.current(), requestBuilder, TextMapSetters.REQUEST_BUILDER_SETTER);
    }
}
