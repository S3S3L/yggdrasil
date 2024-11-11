package io.github.s3s3l.yggdrasil.otel.data;

public enum DataType {
    TRACE("trace"),
    LOG("log"),
    METRIC("metric");

    private final String type;

    DataType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

}
