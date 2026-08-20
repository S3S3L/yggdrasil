package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.Objects;

import io.github.s3s3l.yggdrasil.game.core.ui.UiNode;
import io.github.s3s3l.yggdrasil.game.core.ui.UiPoint;

/**
 * Mutable propagation state for one normalized UI input event.
 */
public final class UiInputEvent {

    private final UiEventType type;
    private final UiPoint position;
    private final int pointerId;
    private final int button;
    private final int keyCode;
    private final Character character;
    private final float amountX;
    private final float amountY;
    private final long timestamp;
    private UiNode target;
    private UiNode currentTarget;
    private UiEventPhase phase;
    private boolean propagationStopped;
    private boolean consumed;

    private UiInputEvent(UiEventType type, UiPoint position, int pointerId, int button, int keyCode,
            Character character, float amountX, float amountY, long timestamp) {
        this.type = Objects.requireNonNull(type, "type");
        this.position = position;
        this.pointerId = pointerId;
        this.button = button;
        this.keyCode = keyCode;
        this.character = character;
        this.amountX = amountX;
        this.amountY = amountY;
        this.timestamp = timestamp;
    }

    public static UiInputEvent pointer(UiEventType type, UiPoint position, int pointerId, int button) {
        return new UiInputEvent(type, Objects.requireNonNull(position, "position"), pointerId, button, 0, null,
                0, 0, System.nanoTime());
    }

    public static UiInputEvent scroll(float amountX, float amountY) {
        return new UiInputEvent(UiEventType.SCROLL, null, 0, 0, 0, null, amountX, amountY, System.nanoTime());
    }

    public static UiInputEvent key(UiEventType type, int keyCode) {
        return new UiInputEvent(type, null, 0, 0, keyCode, null, 0, 0, System.nanoTime());
    }

    public static UiInputEvent text(char character) {
        return new UiInputEvent(UiEventType.TEXT_INPUT, null, 0, 0, 0, character, 0, 0, System.nanoTime());
    }

    public UiEventType getType() {
        return type;
    }

    public UiPoint getPosition() {
        return position;
    }

    public int getPointerId() {
        return pointerId;
    }

    public int getButton() {
        return button;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public Character getCharacter() {
        return character;
    }

    public float getAmountX() {
        return amountX;
    }

    public float getAmountY() {
        return amountY;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UiNode getTarget() {
        return target;
    }

    public UiNode getCurrentTarget() {
        return currentTarget;
    }

    public UiEventPhase getPhase() {
        return phase;
    }

    public boolean isPropagationStopped() {
        return propagationStopped;
    }

    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Stops later listeners outside the currently executing node.
     */
    public void stopPropagation() {
        propagationStopped = true;
    }

    /**
     * Marks the event handled and stops propagation.
     */
    public void consume() {
        consumed = true;
        stopPropagation();
    }

    public void setTarget(UiNode target) {
        this.target = target;
    }

    public void setCurrentTarget(UiNode currentTarget) {
        this.currentTarget = currentTarget;
    }

    public void setPhase(UiEventPhase phase) {
        this.phase = phase;
    }
}
