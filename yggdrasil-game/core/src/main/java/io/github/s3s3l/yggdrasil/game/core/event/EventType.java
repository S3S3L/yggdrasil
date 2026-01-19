package io.github.s3s3l.yggdrasil.game.core.event;

public enum EventType {
    // Pointer events
    POINTER_DOWN,
    POINTER_UP,
    POINTER_MOVE,

    // Keyboard events
    KEY_DOWN,
    KEY_UP,

    // High-level gestures
    CLICK,
    DRAG,
    DROP,
    HOVER,
    SCROLL,
    HOLD,

    CUSTOM;

}
