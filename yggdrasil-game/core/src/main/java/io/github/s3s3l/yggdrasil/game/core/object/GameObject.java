package io.github.s3s3l.yggdrasil.game.core.object;

import java.util.function.Consumer;

import io.github.s3s3l.yggdrasil.game.core.basic.GameAtom;
import io.github.s3s3l.yggdrasil.game.core.basic.TreeNode;
import io.github.s3s3l.yggdrasil.game.core.event.Event;
import io.github.s3s3l.yggdrasil.game.core.event.EventBinding;
import io.github.s3s3l.yggdrasil.game.core.event.EventCenter;
import io.github.s3s3l.yggdrasil.game.core.event.EventListener;
import io.github.s3s3l.yggdrasil.game.core.event.EventOptions;
import io.github.s3s3l.yggdrasil.game.core.event.EventType;
import io.github.s3s3l.yggdrasil.game.core.object.props.Visible;

/**
 * 游戏对象接口，所有游戏中的对象都应实现此接口
 */
public interface GameObject extends GameAtom, TreeNode<GameObject> {
    Visible getVisible();

    void setVisible(Visible visible);

    boolean isEnable();

    void setEnable(boolean enable);

    default <E extends Event> EventBinding on(EventType type, EventListener<E> listener) {
        return on(type, listener, opts -> {
        });
    }

    default <E extends Event> EventBinding on(EventType type, EventListener<E> listener,
            Consumer<EventOptions> optionsCustomizer) {
        return EventCenter.get().bind(this, type, listener, optionsCustomizer);
    }

}
