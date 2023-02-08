package io.github.s3s3l.yggdrasil.register.etcd.listener;

import io.etcd.jetcd.watch.WatchEvent;
import io.etcd.jetcd.watch.WatchResponse;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEvent;
import io.github.s3s3l.yggdrasil.register.core.event.BasicEventType;
import io.github.s3s3l.yggdrasil.register.core.listener.Listener;
import io.github.s3s3l.yggdrasil.register.core.listener.exception.ListenerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class EtcdListener implements io.etcd.jetcd.Watch.Listener {

    private final Listener<byte[], BasicEventType, BasicEvent> listener;

    @Override
    public void onNext(WatchResponse response) {
        for (WatchEvent event : response.getEvents()) {
            switch (event.getEventType()) {
                case DELETE:
                    listener.onEvent(BasicEvent.builder()
                            .eventType(BasicEventType.DELETE)
                            .data(() -> event.getKeyValue()
                                    .getValue()
                                    .getBytes())
                            .oldData(() -> event.getPrevKV()
                                    .getValue()
                                    .getBytes())
                            .build());
                    break;
                case PUT:
                    listener.onEvent(BasicEvent.builder()
                            .eventType(BasicEventType.CHANGE)
                            .data(() -> event.getKeyValue()
                                    .getValue()
                                    .getBytes())
                            .oldData(() -> event.getPrevKV()
                                    .getValue()
                                    .getBytes())
                            .build());
                    break;
                case UNRECOGNIZED:
                default:
                    log.warn("Unrecognized event. Event: {}", event);
                    break;
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throw new ListenerException(throwable);
    }

    @Override
    public void onCompleted() {
        log.info("Etcd listener finished.");
    }

}
