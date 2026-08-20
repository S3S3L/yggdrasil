package io.github.s3s3l.yggdrasil.game.core.event;

/**
 * 监听器执行结果。
 */
public enum EventResult {
    /** 继续传播 */
    CONTINUE,
    /** 停止传播 */
    STOP,
    /** 停止并阻止默认处理 */
    CONSUME
}
