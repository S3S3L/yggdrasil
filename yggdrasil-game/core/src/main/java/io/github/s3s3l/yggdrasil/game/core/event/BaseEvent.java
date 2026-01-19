package io.github.s3s3l.yggdrasil.game.core.event;

import io.github.s3s3l.yggdrasil.game.core.object.GameObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 可复用的事件基类，内置传播控制与元数据。
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent implements Event {

    @Builder.Default
    protected EventType type = EventType.CUSTOM;

    @Builder.Default
    protected EventPhase phase = EventPhase.TARGET;

    @Builder.Default
    protected long timestamp = System.nanoTime();

    protected GameObject source;

    protected GameObject target;

    protected GameObject current;

    protected boolean propagationStopped;

    protected boolean defaultPrevented;

    @Override
    public boolean isPropagationStopped() {
        return propagationStopped;
    }

    @Override
    public void stopPropagation() {
        this.propagationStopped = true;
    }

    @Override
    public boolean isDefaultPrevented() {
        return defaultPrevented;
    }

    @Override
    public void preventDefault() {
        this.defaultPrevented = true;
    }
}
