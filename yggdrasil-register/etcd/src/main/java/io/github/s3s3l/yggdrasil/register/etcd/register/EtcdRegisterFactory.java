package io.github.s3s3l.yggdrasil.register.etcd.register;

import java.net.URI;

import io.etcd.jetcd.Client;
import io.github.s3s3l.yggdrasil.register.core.register.RegisterFactory;
import io.github.s3s3l.yggdrasil.register.core.register.RegisterType;
import io.github.s3s3l.yggdrasil.register.etcd.key.EtcdKeyGenerator;

public class EtcdRegisterFactory implements RegisterFactory<EtcdRegister> {

    @Override
    public EtcdRegister getRegister(URI uri) {
        Client client = Client.builder()
                .endpoints(uri.getSchemeSpecificPart())
                .build();
        return new EtcdRegister(client, new EtcdKeyGenerator());
    }

    @Override
    public String getScheme() {
        return RegisterType.ETCD.schema();
    }

}
