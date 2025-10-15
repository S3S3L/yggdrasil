package io.github.s3s3l.yggdrasil.game.core.snapshot;

import java.util.HashMap;
import java.util.Map;

import io.github.s3s3l.yggdrasil.game.core.basic.GameAtom;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import lombok.Data;
import lombok.Setter;

/**
 * 
 * 游戏对象快照，用于保存游戏对象在某一时刻的状态。
 * 快照包含对象的ID、类型、属性以及所有状态的序列化表示。
 * 需要保证本对象中的所有属性都可以被序列化。
 * 
 * @author s3s3l
 * @since 1.0.0
 */
@Data
public class ObjectSnapshot extends GameObjectConfig implements Snapshot {
    private long id;
    @Setter(lombok.AccessLevel.NONE)
    protected Map<String, StateSnapshot> states = new HashMap<>();

    public ObjectSnapshot(long id, Class<? extends GameAtom> type) {
        this.id = id;
        this.type = type;
    }

    public ObjectSnapshot addProps(String key, String value) {
        this.props.put(key, value);
        return this;
    }

    public ObjectSnapshot addState(String name, StateSnapshot snapshot) {
        this.states.put(name, snapshot);
        return this;
    }

    @Override
    public SnapshotType type() {
        return SnapshotType.OBJECT;
    }
}
