package io.github.s3s3l.yggdrasil.register.nacos.register;

import java.net.URI;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;

import io.github.s3s3l.yggdrasil.register.core.register.RegisterFactory;
import io.github.s3s3l.yggdrasil.register.core.register.RegisterType;
import io.github.s3s3l.yggdrasil.register.core.register.exception.RegisterException;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public class NacosRegisterFactory implements RegisterFactory<NacosRegister> {

    @Override
    public NacosRegister getRegister(URI uri) {

        NamingService namingService;
        try {
            namingService = NacosFactory.createNamingService(uri.getSchemeSpecificPart());
        } catch (NacosException e) {
            throw new RegisterException(e);
        }
        return new NacosRegister(namingService, JacksonUtils.JSON);
    }

    @Override
    public String getScheme() {
        return RegisterType.NACOS.schema();
    }

}
