package io.github.s3s3l.yggdrasil.game._2d.event.mouse;

import io.github.s3s3l.yggdrasil.game.core.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 鼠标点击事件
 * 
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MouseClickEvent implements Event {
    /**
     * 鼠标按键
     * 
     * @see io.github.s3s3l.yggdrasil.game.core.basic.mouse.MouseButton
     */
    private int button;
    private float x;
    private float y;
    
}
