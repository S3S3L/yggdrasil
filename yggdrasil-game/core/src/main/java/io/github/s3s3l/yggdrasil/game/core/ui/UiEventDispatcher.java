package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventListener;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiEventPhase;
import io.github.s3s3l.yggdrasil.game.core.ui.input.UiInputEvent;

/**
 * Routes events through capture, target, and bubble phases while isolating
 * individual listener failures.
 */
final class UiEventDispatcher {

    private final Consumer<Throwable> failureHandler;

    UiEventDispatcher(Consumer<Throwable> failureHandler) {
        this.failureHandler = Objects.requireNonNull(failureHandler, "failureHandler");
    }

    boolean dispatch(UiInputEvent event, UiNode target) {
        event.setTarget(target);
        List<UiNode> path = pathToRoot(target);

        for (int index = path.size() - 1; index > 0 && !event.isPropagationStopped(); index--) {
            invoke(path.get(index), UiEventPhase.CAPTURE, event);
        }
        if (!event.isPropagationStopped()) {
            invoke(target, UiEventPhase.TARGET, event);
        }
        for (int index = 1; index < path.size() && !event.isPropagationStopped(); index++) {
            invoke(path.get(index), UiEventPhase.BUBBLE, event);
        }
        return event.isConsumed();
    }

    private List<UiNode> pathToRoot(UiNode target) {
        List<UiNode> path = new ArrayList<>();
        for (UiNode node = target; node != null; node = node.getUiParent()) {
            path.add(node);
        }
        return path;
    }

    private void invoke(UiNode node, UiEventPhase phase, UiInputEvent event) {
        event.setCurrentTarget(node);
        event.setPhase(phase);
        for (UiEventListener listener : node.listeners(event.getType(), phase)) {
            try {
                listener.handle(event);
            } catch (Throwable failure) {
                try {
                    failureHandler.accept(failure);
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
