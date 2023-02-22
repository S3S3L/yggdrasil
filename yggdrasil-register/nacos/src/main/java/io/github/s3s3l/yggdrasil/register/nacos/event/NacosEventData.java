package io.github.s3s3l.yggdrasil.register.nacos.event;

import java.util.List;

import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NacosEventData {
    private String serviceName;
    private String groupName;
    private String clusters;
    private List<Instance> instances;

    public static NacosEventData fromNamingEvent(NamingEvent namingEvent) {
        return NacosEventData.builder()
                .serviceName(namingEvent.getServiceName())
                .groupName(namingEvent.getGroupName())
                .clusters(namingEvent.getClusters())
                .instances(namingEvent.getInstances())
                .build();
    }
}
