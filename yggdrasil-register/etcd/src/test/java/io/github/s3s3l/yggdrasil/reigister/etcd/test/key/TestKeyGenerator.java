package io.github.s3s3l.yggdrasil.reigister.etcd.test.key;

import io.github.s3s3l.yggdrasil.register.core.key.KeyGenerator;
import io.github.s3s3l.yggdrasil.register.core.node.Node;

public class TestKeyGenerator implements KeyGenerator {

    @Override
    public <T extends Node> String getRegisterKey(T node, String id) {
        return String.join("_", "test", node.getGroup(), node.getHost());
    }

}
