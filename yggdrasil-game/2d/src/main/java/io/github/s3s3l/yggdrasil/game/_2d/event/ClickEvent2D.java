package io.github.s3s3l.yggdrasil.game._2d.event;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.event.ClickEvent;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 2D点击事件
 * 
 */
@Data
@SuperBuilder
public class ClickEvent2D extends ClickEvent<Vector2D> {
    
}
