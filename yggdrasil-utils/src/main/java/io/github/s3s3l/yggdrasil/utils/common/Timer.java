package io.github.s3s3l.yggdrasil.utils.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Timer implements AutoCloseable {

    private static ThreadLocal<Timer> current = new ThreadLocal<>();

    private final BlockingQueue<TimePiece> queue = new LinkedBlockingQueue<>();
    private final Consumer<TimePiece> handler;

    private boolean isRuning = false;
    private long start;
    private long pre;
    private Thread worker;

    public Timer() {
        this.handler = piece -> System.out.println(String.format("[%s] total cost: %dms; delta cost: %dms", piece.name,
                piece.that - piece.start, piece.that - piece.pre));
    }

    public Timer(Consumer<TimePiece> handler) {
        this.handler = handler;
    }

    public synchronized void start() {
        if (current.get() != null) {
            throw new TimerException("A timer is already running in current thread.");
        }
        worker = new Thread(() -> {
            while (isRuning) {
                try {
                    handler.accept(queue.take());
                } catch (InterruptedException e) {
                    log.error("tick error.", e);
                }
            }
        });
        isRuning = true;
        start = System.currentTimeMillis();
        pre = start;
        current.set(this);
        worker.start();
    }

    public static boolean tickCurrent() {
        return tickCurrent("");
    }

    public static boolean tickCurrent(String name) {
        if (current.get() == null) {
            throw new TimerException("No timer started in current thread.");
        }
        return current.get()
                .tick(name);
    }

    public synchronized boolean tick() {
        return tick("");
    }

    public synchronized boolean tick(String name) {
        if (!isRuning) {
            throw new TimerException("Timer not started.");
        }
        long now = System.currentTimeMillis();
        boolean tickResult = queue.offer(TimePiece.builder()
                .name(name)
                .that(now)
                .pre(pre)
                .start(start)
                .build());
        pre = now;
        return tickResult;
    }

    public void stop() {
        isRuning = false;
        current.remove();
    }

    @Override
    public void close() throws Exception {
        stop();
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimePiece {
        private String name;
        private Long start;
        private Long pre;
        private Long that;
    }

    public static class TimerException extends RuntimeException {

        public TimerException() {
        }

        public TimerException(String message) {
            super(message);
        }

        public TimerException(Throwable cause) {
            super(cause);
        }

        public TimerException(String message, Throwable cause) {
            super(message, cause);
        }

        public TimerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

    }
}
