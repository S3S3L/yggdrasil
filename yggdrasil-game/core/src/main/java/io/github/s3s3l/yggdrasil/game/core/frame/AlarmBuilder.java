package io.github.s3s3l.yggdrasil.game.core.frame;

import java.util.function.Consumer;

public abstract class AlarmBuilder {

    protected Consumer<Float> onRing;

    public static FixedAlarmBuilder fixed() {
        return new FixedAlarmBuilder();
    }

    public static VariableAlarmBuilder variable() {
        return new VariableAlarmBuilder();
    }

    public AlarmBuilder onRing(Consumer<Float> onRing) {
        this.onRing = onRing;
        return this;
    }

    public abstract Alarm build();
}
