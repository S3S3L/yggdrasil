package io.github.s3s3l.yggdrasil.game.core.frame;

import java.util.function.Consumer;

public class FixedAlarm implements Alarm {
    private final long intervalMillis;
    private final Consumer<Float> onRing;

    private long lastTriggerTime;
    private boolean running = false;

    public FixedAlarm(long intervalMillis, Consumer<Float> onRing) {
        this.intervalMillis = intervalMillis;
        this.onRing = onRing;
    }

    @Override
    public void start() {
        this.lastTriggerTime = System.currentTimeMillis();
        running = true;

        while (running) {
            var currentTime = System.currentTimeMillis();
            var elapsed = currentTime - lastTriggerTime;
            var sleep = intervalMillis - elapsed;
            if (sleep <= 0) {
                onRing.accept(elapsed / 1000f);
                lastTriggerTime = currentTime;
            } else {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void pause() {
        running = false;
    }

}
