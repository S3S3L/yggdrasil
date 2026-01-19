package io.github.s3s3l.yggdrasil.game.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 指针事件，包含位置与按键信息。
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PointerEvent<V> extends BaseEvent {
    protected int button;
    protected int pointerId;
    protected V position;
    protected V delta;
}
