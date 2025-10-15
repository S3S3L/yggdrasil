package io.github.s3s3l.yggdrasil.game.core.frame;

public class VariableAlarmBuilder extends AlarmBuilder {

    @Override
    public Alarm build() {
        return new VariableAlarm(onRing);
    }
}
