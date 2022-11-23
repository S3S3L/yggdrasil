package org.s3s3l.yggdrasil.starter.cache;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings({ "rawtypes" })
public class HttpCacheKey {
    private String path;
    private String method;
    private Map params;
    private byte[] body;
}
