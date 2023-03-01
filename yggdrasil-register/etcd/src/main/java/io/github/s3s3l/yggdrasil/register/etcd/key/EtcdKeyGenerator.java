package io.github.s3s3l.yggdrasil.register.etcd.key;

import io.github.s3s3l.yggdrasil.register.core.key.KeyGenerator;
import io.github.s3s3l.yggdrasil.register.core.node.Node;

public class EtcdKeyGenerator implements KeyGenerator {

    @Override
    public <T extends Node> String getRegisterKey(T node, String id) {
        return String.join("_", "yggdrasil", node.getGroup(), node.getHost());
    }
    
}
