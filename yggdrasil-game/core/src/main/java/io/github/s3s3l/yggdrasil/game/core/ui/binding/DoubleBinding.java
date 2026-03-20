package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.function.Supplier;

/**
 * Double 绑定
 */
public class DoubleBinding extends ObjectBinding<Double> {

    private final Supplier<Double> computer;

    public DoubleBinding(Supplier<Double> computer, ObservableValue<?>... dependencies) {
        super(dependencies);
        this.computer = computer;
    }

    @Override
    protected Double computeValue() {
        return computer.get();
    }
}
