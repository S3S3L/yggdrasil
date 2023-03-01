package io.github.s3s3l.yggdrasil.register.core.register;

public enum RegisterType {
    ZOOKEEPER("zookeeper"),
    NACOS("nacos"),
    ETCD("etcd");

    private final String schema;

    private RegisterType(String schema) {
        this.schema = schema;
    }

    public String schema() {
        return this.schema;
    }
}
