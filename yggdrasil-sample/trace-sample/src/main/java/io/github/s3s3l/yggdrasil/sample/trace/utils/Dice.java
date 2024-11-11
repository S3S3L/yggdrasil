package io.github.s3s3l.yggdrasil.sample.trace.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Dice {

    private int min;
    private int max;

    private final ExecutorService executorService;

    public Dice(int min, int max, ExecutorService executorService) {
        this.min = min;
        this.max = max;
        this.executorService = executorService;
    }

    public List<Integer> rollTheDice(int rolls) {
        List<Callable<Integer>> tasks = new ArrayList<>(rolls);
        for (int i = 0; i < rolls; i++) {
            tasks.add(() -> rollOnce());
        }

        try {
            return executorService.invokeAll(tasks)
                    .stream().map(t -> {
                        try {
                            return t.get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int rollOnce() {
        return ThreadLocalRandom.current()
                .nextInt(this.min, this.max + 1);
    }

    public static class DBHost {
        String name;
        String user;
        String password;
        String sid;
        String hostAddress;
        String driver = "oracle.jdbc.driver.OracleDriver";
        String connectionUrl;
        int port;

        public String getName() {
            return name;
        }

        public String dbHostName() {
            return name != null ? name : hostAddress;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public String getConnectionUrl() {
            return connectionUrl;
        }

        public String getSid() {
            return sid;
        }

        public String getHostAddress() {
            return hostAddress;
        }

        public String getDriver() {
            return driver;
        }

        public int getPort() {
            return port;
        }

        public String getUrl() {
            return String.format("jdbc:oracle:thin:@%s:%s:%s", hostAddress, port, sid);
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setConnectionUrl(String connectionUrl) {
            this.connectionUrl = connectionUrl;
        }
    }
}
