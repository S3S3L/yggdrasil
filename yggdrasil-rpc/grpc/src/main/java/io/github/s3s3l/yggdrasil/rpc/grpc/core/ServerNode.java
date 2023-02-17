package io.github.s3s3l.yggdrasil.rpc.grpc.core;

import io.github.s3s3l.yggdrasil.register.core.node.Node;
import io.github.s3s3l.yggdrasil.rpc.core.server.ServerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServerNode extends Node {
    private ServerStatus status;
    private String group;
}
