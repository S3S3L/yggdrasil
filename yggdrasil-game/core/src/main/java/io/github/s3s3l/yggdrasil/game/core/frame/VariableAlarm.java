package io.github.s3s3l.yggdrasil.game.core.frame;

import java.util.function.Consumer;

public class VariableAlarm implements Alarm {
    private final Consumer<Float> onRing;

    private long lastTriggerTime;
    private boolean running = false;

    public VariableAlarm(Consumer<Float> onRing) {
        this.onRing = onRing;
    }

    @Override
    public void start() {
        this.lastTriggerTime = System.currentTimeMillis();
        running = true;

        while (running) {
            var currentTime = System.currentTimeMillis();
            var elapsed = currentTime - lastTriggerTime;
            onRing.accept(elapsed / 1000f);
            lastTriggerTime = currentTime;
        }
    }

    @Override
    public void pause() {
        running = false;
    }
    
}
