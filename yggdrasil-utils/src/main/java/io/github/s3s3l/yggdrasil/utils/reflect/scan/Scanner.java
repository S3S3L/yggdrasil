package io.github.s3s3l.yggdrasil.utils.reflect.scan;

import java.util.Set;

public interface Scanner {

    Set<Class<?>> scan(String... packages);
}
