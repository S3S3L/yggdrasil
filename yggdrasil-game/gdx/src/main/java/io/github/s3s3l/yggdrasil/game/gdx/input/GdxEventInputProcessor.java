package io.github.s3s3l.yggdrasil.game.gdx.input;

import com.badlogic.gdx.InputProcessor;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.event.EventDispatcher;
import io.github.s3s3l.yggdrasil.game.core.event.EventType;
import io.github.s3s3l.yggdrasil.game.core.event.KeyEvent;
import io.github.s3s3l.yggdrasil.game.core.event.PointerEvent;
import io.github.s3s3l.yggdrasil.game.core.event.ScrollEvent;
import io.github.s3s3l.yggdrasil.game.core.event.input.HitTester;
import io.github.s3s3l.yggdrasil.game.core.object.GameObject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GdxEventInputProcessor implements InputProcessor {

    private final EventDispatcher dispatcher;
    private final HitTester hitTester;

    @Override
    public boolean keyDown(int keycode) {
        KeyEvent event = KeyEvent.builder()
                .type(EventType.KEY_DOWN)
                .keyCode(keycode)
                .build();
        dispatcher.dispatch(event);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        KeyEvent event = KeyEvent.builder()
                .type(EventType.KEY_UP)
                .keyCode(keycode)
                .build();
        dispatcher.dispatch(event);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        GameObject target = resolveTarget(screenX, screenY);
        if (target == null) {
            return false;
        }
        PointerEvent<Vector2D> event = basePointer(EventType.POINTER_DOWN, button, pointer, screenX, screenY, 0, 0, target);
        dispatcher.dispatch(event);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        GameObject target = resolveTarget(screenX, screenY);
        if (target == null) {
            return false;
        }
        PointerEvent<Vector2D> event = basePointer(EventType.POINTER_UP, button, pointer, screenX, screenY, 0, 0, target);
        dispatcher.dispatch(event);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        GameObject target = resolveTarget(screenX, screenY);
        if (target == null) {
            return false;
        }
        PointerEvent<Vector2D> event = basePointer(EventType.POINTER_MOVE, -1, pointer, screenX, screenY, 0, 0, target);
        dispatcher.dispatch(event);
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        GameObject target = resolveTarget(screenX, screenY);
        if (target == null) {
            return false;
        }
        PointerEvent<Vector2D> event = basePointer(EventType.POINTER_MOVE, -1, 0, screenX, screenY, 0, 0, target);
        dispatcher.dispatch(event);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        ScrollEvent event = ScrollEvent.builder()
                .type(EventType.SCROLL)
                .amountX(amountX)
                .amountY(amountY)
                .build();
        dispatcher.dispatch(event);
        return false;
    }

    private GameObject resolveTarget(float x, float y) {
        if (hitTester == null) {
            return null;
        }
        return hitTester.hit(x, y);
    }

    private PointerEvent<Vector2D> basePointer(EventType type, int button, int pointer, float x, float y, float dx, float dy,
            GameObject target) {
        return PointerEvent.<Vector2D>builder()
                .type(type)
                .button(button)
                .pointerId(pointer)
                .position(Vector2D.builder().x(x).y(y).build())
                .delta(Vector2D.builder().x(dx).y(dy).build())
                .source(target)
                .target(target)
                .build();
    }
}
