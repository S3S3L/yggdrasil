package io.github.s3s3l.yggdrasil.register.core.key;

import io.github.s3s3l.yggdrasil.register.core.node.Node;

public interface KeyGenerator {
    <T extends Node> String getRegisterKey(T node, String id);
}
