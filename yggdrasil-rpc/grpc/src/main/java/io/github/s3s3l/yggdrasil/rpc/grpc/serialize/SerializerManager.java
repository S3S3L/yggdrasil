package io.github.s3s3l.yggdrasil.rpc.grpc.serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializerManager {
    private Map<String, Serializer> serializers = new ConcurrentHashMap<>();

    public Serializer getSerializer(String serializeType) {
        return serializers.get(serializeType);
    }
}
