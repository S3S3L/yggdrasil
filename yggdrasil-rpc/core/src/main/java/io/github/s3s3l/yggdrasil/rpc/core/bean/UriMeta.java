package io.github.s3s3l.yggdrasil.rpc.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UriMeta {
    private String scheme;
    private String host;
    private int port;
    private String resource;

}
