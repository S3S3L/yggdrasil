package io.github.s3s3l.yggdrasil.rpc.core.bean;

public enum ResponseCode {
    OK("ok"),
    SERVICE_NOT_FOUND("Service not found.");

    private final String defMsg;

    private ResponseCode(String defMsg) {
        this.defMsg = defMsg;
    }

    public String defMsg() {
        return this.defMsg;
    }
}
