package io.github.s3s3l.yggdrasil.game.core.frame;

public class FixedAlarmBuilder extends AlarmBuilder {

    private long interval;

    /**
     * Set the interval of the alarm in milliseconds
     * @param interval interval in milliseconds
     * @return this builder
     */
    public FixedAlarmBuilder interval(long interval) {
        this.interval = interval;
        return this;
    }

    @Override
    public Alarm build() {
        return new FixedAlarm(interval, onRing);
    }
    
}
