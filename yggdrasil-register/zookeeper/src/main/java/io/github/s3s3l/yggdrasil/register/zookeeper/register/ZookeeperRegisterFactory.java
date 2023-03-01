package io.github.s3s3l.yggdrasil.register.zookeeper.register;

import java.net.URI;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;

import io.github.s3s3l.yggdrasil.register.core.register.RegisterFactory;
import io.github.s3s3l.yggdrasil.register.core.register.RegisterType;
import io.github.s3s3l.yggdrasil.register.zookeeper.key.ZookeeperKeyGenerator;

public class ZookeeperRegisterFactory implements RegisterFactory<ZookeeperRegister> {

    @Override
    public ZookeeperRegister getRegister(URI uri) {
        CuratorFramework curator = CuratorFrameworkFactory.newClient(uri.getSchemeSpecificPart(),
                new RetryOneTime(100));
        curator.start();
        return new ZookeeperRegister(curator, new ZookeeperKeyGenerator());
    }

    @Override
    public String getScheme() {
        return RegisterType.ZOOKEEPER.schema();
    }

}
