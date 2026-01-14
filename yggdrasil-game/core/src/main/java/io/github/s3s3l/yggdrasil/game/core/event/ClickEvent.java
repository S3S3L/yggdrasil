package io.github.s3s3l.yggdrasil.game.core.event;

import lombok.AllArgsConstructor;
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
public abstract class ClickEvent<V> implements Event {
    /**
     * 按键
     * 
     * @see io.github.s3s3l.yggdrasil.game.core.basic.mouse.MouseButton
     */
    protected int button;
    protected V position;
    
}
