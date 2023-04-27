package io.github.s3s3l.yggdrasil.boot.bean.lifecycle;

import io.github.s3s3l.yggdrasil.bean.Sortable;
import io.github.s3s3l.yggdrasil.boot.bean.BeanLifecycle;

public interface BeanLifeCycleWorker extends Sortable, Comparable<BeanLifeCycleWorker> {

    <T> T process(T pre, BeanContext context);

    BeanLifecycle getLifecycle();

    @Override
    default int compareTo(BeanLifeCycleWorker o) {
        if (o == null) {
            return -1;
        }

        return this.getPriority() - o.getPriority();
    }
}
