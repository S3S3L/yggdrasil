package io.github.s3s3l.yggdrasil.register.etcd.listener;

import io.etcd.jetcd.Watch.Watcher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EtcdListenerMeta {
    private Watcher watcher;
    private EtcdListener listener;
}
