package io.github.s3s3l.yggdrasil.rpc.grpc.serialize;

public interface Serializer {
    <T> T toObject(byte[] buf, Class<T> type);

    byte[] toBytes(Object data);

    String name();
}
