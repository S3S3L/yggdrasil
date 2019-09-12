package org.s3s3l.yggdrasil.utils.concurrent.remote;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * <p>
 * </p>
 * ClassName:RemoteCaller <br>
 * Date: Dec 19, 2018 4:15:13 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class RemoteCaller<T> {
    protected long timeout;
    protected Function<TimeoutException, T> onTimeout;
    protected Function<ExecutionException, T> onException;
    protected TimeUnit timeUnit;

    public abstract T call(Callable<T> call) throws InterruptedException;

    public static class CallerBuilder<T> {

        private RemoteCaller<T> caller;

        private CallerBuilder() {

        }

        public static <T> CallerBuilder<T> create(RemoteCaller<T> caller) {
            CallerBuilder<T> builder = new CallerBuilder<>();
            builder.caller = caller;
            return builder;
        }

        public CallerBuilder<T> timeout(long timeout, TimeUnit timeUnit) {
            caller.timeout = timeout;
            caller.timeUnit = timeUnit;
            return this;
        }

        public CallerBuilder<T> onTimeout(Function<TimeoutException, T> supplier) {
            caller.onTimeout = supplier;
            return this;
        }

        public CallerBuilder<T> onException(Function<ExecutionException, T> supplier) {
            caller.onException = supplier;
            return this;
        }

        public RemoteCaller<T> build() {
            return caller;
        }

    }

}
