package io.github.s3s3l.yggdrasil.game.core.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.s3s3l.yggdrasil.game.core.config.Props;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.core.snapshot.StateSnapshot;
import io.github.s3s3l.yggdrasil.game.core.state.State;
import io.github.s3s3l.yggdrasil.game.core.utils.IdGenerator;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractGameAtom<T extends GameObjectConfig> implements GameAtom {

    protected final AtomicBoolean dirty = new AtomicBoolean(false);
    /**
     * 属性，初始化后不可变更
     */
    protected final Props props;
    /**
     * 状态，初始化后可变更，运行时可追加和删除，变更后会标记当前对象为脏对象
     */
    protected final Map<String, State<?>> states = new HashMap<>();

    @Getter
    @Setter
    protected long id;

    public AbstractGameAtom(T config) {
        if (!config.getType().isAssignableFrom(getClass())) {
            throw new IllegalArgumentException(
                    "Config type not match, expect " + this.getClass() + " but " + config.getType());
        }
        this.id = IdGenerator.nextId();
        this.props = config.getProps();
    }

    /**
     * 内部调用，从snapshot中恢复对象状态，会在restore方法中所有状态恢复后调用
     * 
     * @param snapshot 快照对象
     */
    protected abstract void doRestore(ObjectSnapshot snapshot);

    /**
     * 内部调用，将对象状态写入快照，会在snapshot方法中所有状态和属性写入后调用
     * 
     * @param snapshot 快照对象
     */
    protected abstract void doSnapshot(ObjectSnapshot snapshot);

    /**
     * 内部调用，计算对象状态，会在calculate方法中调用
     * @param delta 时间增量，单位：秒
     */
    protected abstract void doCalculate(float delta);

    @Override
    public void calculate(float delta) {

        doCalculate(delta);
    }

    /**
     * 内部调用，预渲染对象，会在preRender方法中调用
     */
    protected abstract void doPreRender();

    @Override
    public void preRender() {
        doPreRender();
    }

    /**
     * 内部调用，渲染对象，会在render方法中调用
     */
    protected abstract void doRender();

    @Override
    public void render() {
        doRender();
        clearDirty();
    }

    @Override
    public ObjectSnapshot snapshot() {
        var snapshot = new ObjectSnapshot(this.id, this.getClass());
        for (String propName : props.stringPropertyNames()) {
            snapshot.addProps(propName, props.getProperty(propName));
        }
        for (var state : states.entrySet()) {
            snapshot.addState(state.getKey(),
                    StateSnapshot.builder()
                            .type(state.getValue().getType())
                            .data(JacksonUtils.NONNULL_JSON.valueToTree(state.getValue().getClone()))
                            .build());
        }

        doSnapshot(snapshot);
        return snapshot;
    }

    @Override
    public void restore(ObjectSnapshot snapshot) {
        this.id = snapshot.getId();
        states.clear();

        for (var stateEntry : snapshot.getStates().entrySet()) {
            var value = stateEntry.getValue();
            createState(stateEntry.getKey(), JacksonUtils.NONNULL_JSON.treeToValue(value.getData(), value.getType()),
                    value.getType());
        }

        doRestore(snapshot);
    }

    @Override
    public boolean isDirty() {
        return dirty.get();
    }

    @Override
    public void markDirty() {
        dirty.compareAndSet(false, true);
    }

    @Override
    public void clearDirty() {
        dirty.compareAndSet(true, false);
    }

    // protected methods

    protected <D> State<D> createState(String name, Object data, Class<D> clazz) {
        if (states.containsKey(name)) {
            throw new IllegalArgumentException("State with name " + name + " already exists.");
        }

        State<D> state = new State<>(clazz.cast(data), clazz, this);
        states.put(name, state);
        return state;
    }

    @SuppressWarnings("unchecked")
    protected <D> State<D> getState(String name, Class<D> clazz) {
        State<?> state = states.get(name);
        if (state == null) {
            throw new IllegalArgumentException("State with name " + name + " does not exist.");
        }
        if (!state.getType().equals(clazz)) {
            throw new IllegalArgumentException("State with name " + name + " is not of type " + clazz.getName());
        }
        return (State<D>) state;
    }

}
