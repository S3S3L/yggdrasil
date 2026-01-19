package io.github.s3s3l.yggdrasil.game.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 点击事件
 * 
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class ClickEvent<V> extends PointerEvent<V> {
    @Builder.Default
    protected EventType type = EventType.CLICK;
}
