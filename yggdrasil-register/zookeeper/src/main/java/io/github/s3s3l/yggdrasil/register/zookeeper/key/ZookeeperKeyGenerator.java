package io.github.s3s3l.yggdrasil.register.zookeeper.key;

import io.github.s3s3l.yggdrasil.register.core.key.KeyGenerator;
import io.github.s3s3l.yggdrasil.register.core.node.Node;

public class ZookeeperKeyGenerator implements KeyGenerator {

    @Override
    public <T extends Node> String getRegisterKey(T node, String id) {
        return String.join("/", "", "yggdrasil", node.getGroup(), node.getHost());
    }

}
