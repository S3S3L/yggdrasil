package io.github.s3s3l.yggdrasil.game.core.basic;

import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;

public interface Storable extends ID {

    /**
     * 获取当前对象的状态快照
     * 
     * @return
     */
    ObjectSnapshot snapshot();

    /**
     * 根据提供的快照恢复对象状态
     * 
     * @param snapshot
     */
    void restore(ObjectSnapshot snapshot);
}
