package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.function.Supplier;

/**
 * 布尔绑定
 */
public class BooleanBinding extends ObjectBinding<Boolean> {

    private final Supplier<Boolean> computer;

    public BooleanBinding(Supplier<Boolean> computer, ObservableValue<?>... dependencies) {
        super(dependencies);
        this.computer = computer;
    }

    @Override
    protected Boolean computeValue() {
        return computer.get();
    }
}
