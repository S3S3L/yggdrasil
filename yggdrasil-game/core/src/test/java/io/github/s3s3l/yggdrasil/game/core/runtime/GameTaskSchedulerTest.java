package io.github.s3s3l.yggdrasil.game.core.runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GameTaskSchedulerTest {

    @Test
    void serialSchedulerRunsAllTasksOnTheCallingThreadAndIsolatesFailures() {
        List<String> calls = new ArrayList<>();
        String callingThread = Thread.currentThread().getName();
        GameTaskScheduler scheduler = GameTaskScheduler.serial();

        assertThrows(IllegalStateException.class, () -> scheduler.execute(List.of(
                () -> calls.add("first:" + Thread.currentThread().getName()),
                () -> {
                    throw new IllegalStateException("broken");
                },
                () -> calls.add("last:" + Thread.currentThread().getName()))));

        assertEquals(List.of("first:" + callingThread, "last:" + callingThread), calls);
    }
}
