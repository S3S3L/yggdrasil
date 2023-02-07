package io.github.s3s3l.yggdrasil.register.nacos.listener;

import com.alibaba.nacos.api.naming.listener.EventListener;

import io.github.s3s3l.yggdrasil.register.core.node.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NacosListenerMeta {
    private Node node;
    private EventListener eventListener;
}
