package io.github.s3s3l.yggdrasil.game.core.event.listener;

import io.github.s3s3l.yggdrasil.game.core.event.ClickEvent;

public interface ClickEventListener<V> {
    
    void onClick(ClickEvent<V> event);
}
