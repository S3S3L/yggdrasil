package io.github.s3s3l.yggdrasil.boot.bean.lifecycle;

import io.github.s3s3l.yggdrasil.boot.bean.BeanLifecycle;
import io.github.s3s3l.yggdrasil.boot.bean.box.BeanBox;

public interface BeanPipline {

    <T> T process(BeanBox beanBox, BeanLifecycle lifecycle);
}
