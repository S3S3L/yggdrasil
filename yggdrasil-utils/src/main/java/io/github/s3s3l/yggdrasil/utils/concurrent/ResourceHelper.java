package io.github.s3s3l.yggdrasil.utils.concurrent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

/**
 * <p>
 * </p>
 * ClassName:ResourceHelper <br>
 * Date: May 16, 2019 4:24:23 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class ResourceHelper {

    public static <T> T readFromResource(Supplier<T> supplier, ReadWriteLock... locks) {
        for (ReadWriteLock lock : locks) {
            lock.readLock()
                    .lock();
        }
        try {
            return supplier.get();
        } finally {
            for (ReadWriteLock lock : locks) {
                lock.readLock()
                        .unlock();
            }
        }
    }

    public static void reloadResource(Processor processor, ReadWriteLock... locks) {
        for (ReadWriteLock lock : locks) {
            lock.writeLock()
                    .lock();
        }
        try {
            processor.process();
        } finally {
            for (ReadWriteLock lock : locks) {
                lock.writeLock()
                        .unlock();
            }
        }
    }

    @FunctionalInterface
    public static interface Processor {
        void process();
    }
}
