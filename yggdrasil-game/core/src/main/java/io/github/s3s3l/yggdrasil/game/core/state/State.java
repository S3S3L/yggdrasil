package io.github.s3s3l.yggdrasil.game.core.state;

import java.util.function.Function;

import io.github.s3s3l.yggdrasil.game.core.basic.GameAtom;
import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * data中的属性必须要有getter和setter方法，否则无法进行深拷贝
 */
public class State<T> {

    private T data;
    @Getter
    private T clone;
    @Getter
    private final Class<T> type;
    private final GameAtom owner;

    public State(T data, @NonNull Class<T> type, @NonNull GameAtom owner) {
        this.data = data;
        this.clone = ReflectionUtils.deepClone(data, type);
        this.owner = owner;
        this.type = type;
        PropertyDescriptorReflectionBean.warmup(type);
    }

    /**
     * Update the state data using the provided updater function.
     * 
     * @param updater
     */
    public void update(@NonNull Function<T, T> updater) {
        data = updater.apply(data);
        this.owner.markDirty();
        this.clone = ReflectionUtils.deepClone(data, type);
    }

    /**
     * Get the owner of this state.
     * 
     * @return
     */
    public Renderable owner() {
        return owner;
    }
}
