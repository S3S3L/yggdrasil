package io.github.s3s3l.yggdrasil.game.core.event;

import java.util.function.Predicate;

import lombok.Data;

/**
 * 监听器绑定选项。
 */
@Data
public class EventOptions {
    private EventPhase phase = EventPhase.BUBBLE;
    private int priority = 0;
    private boolean once = false;
    private boolean async = false;
    private Predicate<EventContext<? extends Event>> predicate;
    /** 纳秒级节流时间，0 表示不节流 */
    private long throttleNanos = 0L;
    /** 纳秒级防抖时间，0 表示不防抖 */
    private long debounceNanos = 0L;
}
