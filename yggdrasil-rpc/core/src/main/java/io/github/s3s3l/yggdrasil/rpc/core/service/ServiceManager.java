package io.github.s3s3l.yggdrasil.rpc.core.service;

public interface ServiceManager {
    
    void register(ServiceMeta meta);

    ServiceMeta getService(String serviceName);
}
