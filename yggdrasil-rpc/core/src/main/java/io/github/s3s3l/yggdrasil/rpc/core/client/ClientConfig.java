package io.github.s3s3l.yggdrasil.rpc.core.client;

import io.github.s3s3l.yggdrasil.register.core.node.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClientConfig extends Node {
    private String serverName;
}
