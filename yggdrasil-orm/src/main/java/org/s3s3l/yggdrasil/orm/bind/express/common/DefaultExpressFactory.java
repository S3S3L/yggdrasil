package org.s3s3l.yggdrasil.orm.bind.express.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import org.s3s3l.yggdrasil.orm.bind.express.ExpressFactory;
import org.s3s3l.yggdrasil.orm.meta.MetaManager;

/**
 * 
 * <p>
 * </p>
 * ClassName: DefaultExpressFactory <br>
 * date: Sep 20, 2019 11:27:58 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultExpressFactory implements ExpressFactory {

    private final Map<Class<?>, DataBindExpress> dataBindExpresses = new ConcurrentHashMap<>();
    private final Lock expressBindLock = new ReentrantLock();

    @Override
    public <T> DataBindExpress getDataBindExpress(Class<T> modelType, MetaManager metaManager) {
        DataBindExpress dataBindExpress = dataBindExpresses.get(modelType);
        if (dataBindExpress != null) {
            return dataBindExpress;
        }
        expressBindLock.lock();
        try {
            dataBindExpress = new DefaultDataBindExpress(modelType, metaManager);
            dataBindExpresses.put(modelType, dataBindExpress);
            return dataBindExpress;
        } finally {
            expressBindLock.unlock();
        }
    }

}
