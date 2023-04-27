package io.github.s3s3l.yggdrasil.boot.bean.lifecycle;

import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.boot.bean.BeanLifecycle;
import io.github.s3s3l.yggdrasil.boot.bean.box.BeanBox;
import io.github.s3s3l.yggdrasil.boot.exception.BeanCreationException;

public class ScalableBeanPipline implements BeanPipline {
    private Map<BeanLifecycle, SortedSet<BeanLifeCycleWorker>> steps = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings({ "unchecked" })
    public <T> T process(BeanBox box, BeanLifecycle lifecycle) {
        if (lifecycle.isStable()) {
            throw new BeanCreationException("A stable lifecycle stage " + lifecycle + "can`t be processing.");
        }

        if (box.getContext() == null) {
            throw new BeanCreationException("Bean context hasn`t be resolved");
        }

        T bean = (T) box.getBean();
        for (BeanLifeCycleWorker step : steps.get(lifecycle)) {
            bean = step.process(bean, box.getContext());
        }

        return bean;
    }
}
