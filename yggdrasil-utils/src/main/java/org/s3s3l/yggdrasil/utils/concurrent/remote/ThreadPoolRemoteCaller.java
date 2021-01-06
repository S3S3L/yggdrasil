package org.s3s3l.yggdrasil.utils.concurrent.remote;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * </p>
 * ClassName:ThreadPoolRemoteCaller <br>
 * Date: Dec 19, 2018 4:24:26 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ThreadPoolRemoteCaller<T> extends RemoteCaller<T> {
    private ExecutorService executor;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ThreadPoolRemoteCaller(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public T call(Callable<T> call) throws InterruptedException {
        FutureTask<T> future = new FutureTask<>(call);
        executor.execute(future);
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            logger.warn("Remote call exception", e);
            return onException.apply(e);
        } catch (TimeoutException e) {
            logger.warn("Remote call timeout", e);
            return onTimeout.apply(e);
        }
    }

}
