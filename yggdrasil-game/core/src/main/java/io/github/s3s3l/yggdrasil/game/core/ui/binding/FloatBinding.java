package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.function.Supplier;

/**
 * Float 绑定
 */
public class FloatBinding extends ObjectBinding<Float> {

    private final Supplier<Float> computer;

    public FloatBinding(Supplier<Float> computer, ObservableValue<?>... dependencies) {
        super(dependencies);
        this.computer = computer;
    }

    @Override
    protected Float computeValue() {
        return computer.get();
    }
}
