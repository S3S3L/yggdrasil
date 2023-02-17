package io.github.s3s3l.yggdrasil.rpc.core.server;

import io.github.s3s3l.yggdrasil.register.core.node.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ServerConfig extends Node {
    private String register;
}
