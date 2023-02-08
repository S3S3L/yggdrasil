package io.github.s3s3l.yggdrasil.register.etcd.observer;

import io.etcd.jetcd.lease.LeaseKeepAliveResponse;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class LeaseObserver implements StreamObserver<LeaseKeepAliveResponse> {
    private final long id;

    @Override
    public void onNext(LeaseKeepAliveResponse value) {
        log.debug("Etcd LeaseObserver onNext. Id: {}, ttl: {}", value.getID(), value.getTTL());
    }

    @Override
    public void onError(Throwable t) {
        log.debug("Etcd LeaseObserver onError. Id: " + id, t);
    }

    @Override
    public void onCompleted() {
        log.debug("Etcd LeaseObserver onCompleted. Id: {}", id);
    }

}
