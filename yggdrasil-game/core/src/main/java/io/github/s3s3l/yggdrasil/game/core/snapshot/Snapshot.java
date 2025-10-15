package io.github.s3s3l.yggdrasil.game.core.snapshot;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

public interface Snapshot extends ID {
    SnapshotType type();
}
