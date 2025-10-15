package io.github.s3s3l.yggdrasil.game.core.snapshot;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

public class SnapshotManager {
    private static final SnapshotManager INSTANCE = new SnapshotManager();
    
    private SnapshotManager() {
    }

    public static SnapshotManager get() {
        return INSTANCE;
    }

    public void saveSnapshot(ObjectSnapshot snapshot) {

    }
    
    public ObjectSnapshot getLatestSnapshot(ID target) {
        return null;
    }
}
