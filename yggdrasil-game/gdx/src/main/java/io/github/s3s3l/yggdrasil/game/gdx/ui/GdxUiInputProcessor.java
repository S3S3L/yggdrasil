package io.github.s3s3l.yggdrasil.game.gdx.ui;

import java.util.Objects;
import java.util.Optional;

import com.badlogic.gdx.InputProcessor;

import io.github.s3s3l.yggdrasil.game.core.ui.UiPoint;
import io.github.s3s3l.yggdrasil.game.core.ui.UiViewport;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventType;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiInputDispatcher;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiInputEvent;

/**
 * LibGDX input adapter. It only normalizes GDX callbacks; targeting and event
 * propagation remain in the backend-neutral core UI scene.
 */
public final class GdxUiInputProcessor implements InputProcessor {

    private final UiInputDispatcher dispatcher;
    private UiViewport viewport;

    public GdxUiInputProcessor(UiInputDispatcher dispatcher, UiViewport viewport) {
        this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher");
        this.viewport = Objects.requireNonNull(viewport, "viewport");
    }

    public void setViewport(UiViewport viewport) {
        this.viewport = Objects.requireNonNull(viewport, "viewport");
    }

    @Override
    public boolean keyDown(int keycode) {
        return dispatcher.dispatch(UiInputEvent.key(UiEventType.KEY_DOWN, keycode));
    }

    @Override
    public boolean keyUp(int keycode) {
        return dispatcher.dispatch(UiInputEvent.key(UiEventType.KEY_UP, keycode));
    }

    @Override
    public boolean keyTyped(char character) {
        return dispatcher.dispatch(UiInputEvent.text(character));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return dispatchPointer(UiEventType.POINTER_DOWN, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return dispatchPointer(UiEventType.POINTER_UP, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return dispatchPointer(UiEventType.POINTER_MOVE, screenX, screenY, pointer, -1);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return dispatchPointer(UiEventType.POINTER_CANCEL, screenX, screenY, pointer, button);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return dispatchPointer(UiEventType.POINTER_MOVE, screenX, screenY, 0, -1);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return dispatcher.dispatch(UiInputEvent.scroll(amountX, amountY));
    }

    private boolean dispatchPointer(UiEventType type, float screenX, float screenY, int pointer, int button) {
        Optional<UiPoint> point = viewport.toUi(screenX, screenY);
        if (point.isEmpty()) {
            return false;
        }
        UiInputEvent event = UiInputEvent.pointer(type, point.get(), pointer, button);
        boolean consumed = dispatcher.dispatch(event);
        return consumed || event.getTarget() != null;
    }
}
