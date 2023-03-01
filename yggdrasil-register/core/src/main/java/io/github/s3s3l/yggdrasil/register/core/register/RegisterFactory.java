package io.github.s3s3l.yggdrasil.register.core.register;

import java.net.URI;

public interface RegisterFactory<R extends Register<?, ?, ?, ?>> {

    abstract R getRegister(URI uri);

    abstract String getScheme();
}
