package org.s3s3l.yggdrasil.bean.redis;

import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName: HAPClusterNode
 * Date:      2018/4/25 下午9:26 <br>
 *
 * @author carterwang
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
public class HAPClusterNode {

    private List<HAPNode> clusterConfig;
    private GenericObjectPoolConfig<?> poolConfig;
}
