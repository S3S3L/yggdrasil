package io.github.s3s3l.yggdrasil.game.core.event;

import io.github.s3s3l.yggdrasil.game.core.object.GameObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 监听器绑定句柄。
 */
@Getter
@RequiredArgsConstructor
public class EventBinding {
    private final GameObject target;
    private final EventType type;
    private final EventPhase phase;
    private final int priority;
    private final boolean once;
    private final Runnable disposer;

    public void dispose() {
        if (disposer != null) {
            disposer.run();
        }
    }
}
