package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.function.Supplier;

/**
 * 整数绑定
 */
public class IntegerBinding extends ObjectBinding<Integer> {

    private final Supplier<Integer> computer;

    public IntegerBinding(Supplier<Integer> computer, ObservableValue<?>... dependencies) {
        super(dependencies);
        this.computer = computer;
    }

    @Override
    protected Integer computeValue() {
        return computer.get();
    }
}
