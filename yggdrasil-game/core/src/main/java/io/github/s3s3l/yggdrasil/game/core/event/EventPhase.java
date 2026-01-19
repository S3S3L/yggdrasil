package io.github.s3s3l.yggdrasil.game.core.event;

/**
 * 事件传播阶段。
 */
public enum EventPhase {
    /**
     * 从根对象开始，一直到目标对象，逐步向下触发事件（可短路）
     */
    CAPTURE,
    /**
     * 只在目标对象上触发事件
     */
    TARGET,
    /**
     * 从目标对象开始，依次在parent上触发事件，直到parent=null（可短路）
     */
    BUBBLE
}
