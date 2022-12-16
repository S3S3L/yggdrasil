package io.github.s3s3l.yggdrasil.rpc.core.utils;

import java.net.URI;

import io.github.s3s3l.yggdrasil.rpc.core.bean.UriMeta;

public abstract class UriResolver {
    public static UriMeta resolve(URI uri) {
        return UriMeta.builder()
                .scheme(uri.getScheme())
                .host(uri.getHost())
                .port(uri.getPort())
                .resource(uri.getPath())
                .build();
    }
}
