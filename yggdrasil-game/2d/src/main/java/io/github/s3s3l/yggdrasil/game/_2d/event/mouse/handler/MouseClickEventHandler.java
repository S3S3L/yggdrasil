package io.github.s3s3l.yggdrasil.game._2d.event.mouse.handler;

import io.github.s3s3l.yggdrasil.game._2d.event.ClickEvent2D;
import io.github.s3s3l.yggdrasil.game._2d.object.GameObject2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

/**
 * 鼠标点击事件处理器
 * 
 */
public class MouseClickEventHandler<S extends Shape<Vector2D>> implements EventHandler<ClickEvent2D> {

    private GameObject2D<S> target;

    @Override
    public void handle(ClickEvent2D event) {
        // TODO 根据点击位置找到对应的游戏对象，并触发相应的交互逻辑
        if (!target.getShape().contains(event.getPosition())) {
            return;
        }

    }

}
