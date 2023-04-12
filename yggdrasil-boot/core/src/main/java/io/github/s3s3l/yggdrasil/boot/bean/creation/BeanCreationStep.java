package io.github.s3s3l.yggdrasil.boot.bean.creation;

import io.github.s3s3l.yggdrasil.bean.Sortable;

public interface BeanCreationStep extends Sortable {

    <T> T process(T pre, CreationContext<T> context);
}
