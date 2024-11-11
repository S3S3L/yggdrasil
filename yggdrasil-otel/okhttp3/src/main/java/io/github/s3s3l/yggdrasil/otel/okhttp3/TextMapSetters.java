package io.github.s3s3l.yggdrasil.otel.okhttp3;

import io.opentelemetry.context.propagation.TextMapSetter;
import okhttp3.Request;

public class TextMapSetters {
    public static final TextMapSetter<Request.Builder> REQUEST_BUILDER_SETTER = new TextMapSetter<Request.Builder>() {
        @Override
        public void set(Request.Builder carrier, String key, String value) {
            carrier.addHeader(key, value);
        }
    };

}
