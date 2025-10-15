package io.github.s3s3l.yggdrasil.game.core.utils;

public abstract class IdGenerator {
    private static long currentId = 0;

    public static synchronized long nextId() {
        return currentId++;
    }
}
