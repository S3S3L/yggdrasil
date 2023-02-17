package io.github.s3s3l.yggdrasil.rpc.grpc.client;

import io.github.s3s3l.yggdrasil.rpc.core.client.ClientConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcClientConfig extends ClientConfig {
    private String protocol;
}
