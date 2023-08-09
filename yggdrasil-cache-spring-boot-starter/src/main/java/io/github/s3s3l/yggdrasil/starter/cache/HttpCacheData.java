package io.github.s3s3l.yggdrasil.starter.cache;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HttpCacheData {
    private byte[] content;
    @Builder.Default
    private Map<String, String> headers = new HashMap<>();

    public HttpCacheData setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }
}
