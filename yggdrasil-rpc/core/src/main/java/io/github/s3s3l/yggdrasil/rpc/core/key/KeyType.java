package io.github.s3s3l.yggdrasil.rpc.core.key;

public enum KeyType {
    REGISTER("register"), CONFIG("config");

    private final String str;

    private KeyType(String str) {
        this.str = str;
    }

    public String str() {
        return this.str;
    }
}
