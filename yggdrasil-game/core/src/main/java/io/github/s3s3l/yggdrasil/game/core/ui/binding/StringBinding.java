package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.function.Supplier;

/**
 * 字符串绑定
 */
public class StringBinding extends ObjectBinding<String> {

    private final Supplier<String> computer;

    public StringBinding(Supplier<String> computer, ObservableValue<?>... dependencies) {
        super(dependencies);
        this.computer = computer;
    }

    @Override
    protected String computeValue() {
        return computer.get();
    }
}
