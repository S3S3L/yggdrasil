package io.github.s3s3l.yggdrasil.game.core.event;

import io.github.s3s3l.yggdrasil.game.core.object.GameObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 监听器的上下文数据。
 */
@Getter
@RequiredArgsConstructor
public class EventContext<E extends Event> {
    private final E event;
    private final GameObject current;
    /** 帧间隔，可选填充，默认 0 */
    private final float delta;
}
