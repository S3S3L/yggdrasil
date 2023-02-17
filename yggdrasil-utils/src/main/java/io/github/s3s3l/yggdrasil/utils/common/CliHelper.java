package io.github.s3s3l.yggdrasil.utils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:CliHelper <br>
 * Date: Apr 22, 2019 9:29:40 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CliHelper {
    private static final boolean IS_WIN = System.getProperty("os.name")
            .toLowerCase()
            .startsWith("windows");
    private static final long DEFAULT_TIMEOUT = 60L;

    private final Logger logger = LoggerFactory.getLogger(CliHelper.class);
    private final ExecutorService executor;

    public CliHelper(ExecutorService executor) {
        this.executor = executor;
    }

    public CliResponse exec(String cmd, int retry, long waitSeconds) {
        logger.info("Command executing. Origin command: {}", cmd);
        CliResponse res = new CliResponse();
        try {
            ProcessBuilder builder = new ProcessBuilder();
            if (IS_WIN) {
                builder.command("cmd.exe", "/c", cmd);
            } else {
                builder.command("sh", "-c", cmd);
            }
            Process process = builder.start();

            for (int i = 0; i <= retry; i++) {
                res = process(waitSeconds, process);
                if (res.isSuccess()) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new ProcessingException(e);
        }

        if (!res.isSuccess()) {
            logger.warn("Command executing error. Output: \n{}", res.getResponse());
        }

        return res;
    }

    private CliResponse process(long waitSeconds, Process process) {
        CliResponse res = new CliResponse();
        try {
            StringBuilder resp = new StringBuilder();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), msg -> {
                resp.append(msg)
                        .append("\n");
            });
            FutureTask<Void> future = new FutureTask<>(streamGobbler, null);
            executor.execute(future);

            future.get(waitSeconds, TimeUnit.SECONDS);

            res.setCode(process.waitFor());
            res.setResponse(resp.toString());
        } catch (ExecutionException e) {
            logger.error("Command executing error.", e);
        } catch (TimeoutException e) {
            logger.error("Command executing timeout.", e);
        } catch (InterruptedException e) {
            logger.error("Command executing interrupted.", e);
            Thread.currentThread()
                    .interrupt();
        }

        return res;
    }

    public CliResponse exec(String cmd) {
        return exec(cmd, 0, DEFAULT_TIMEOUT);
    }

    @Data
    public static class CliResponse {
        private int code = -1;
        private String response;

        public boolean isSuccess() {
            return code == 0;
        }

        @Override
        public String toString() {
            return "CliResponse [code=" + code + ", response=" + response + "]";
        }
    }

    private static class StreamGobbler implements Runnable {
        private static final Logger logger = LoggerFactory.getLogger(StreamGobbler.class);
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                reader.lines()
                        .forEach(consumer);
            } catch (IOException e) {
                logger.error("Command processing error.", e);
                throw new ProcessingException(e);
            }
        }
    }

    public static class ProcessingException extends RuntimeException {

        /**
         * @since JDK 1.8
         */
        private static final long serialVersionUID = 944385215913132110L;

        public ProcessingException() {
            super();
        }

        public ProcessingException(String message, Throwable cause, boolean enableSuppression,
                boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        public ProcessingException(String message, Throwable cause) {
            super(message, cause);
        }

        public ProcessingException(String message) {
            super(message);
        }

        public ProcessingException(Throwable cause) {
            super(cause);
        }

    }
}
