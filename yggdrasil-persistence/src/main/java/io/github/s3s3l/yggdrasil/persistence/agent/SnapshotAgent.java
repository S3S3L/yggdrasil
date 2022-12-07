package io.github.s3s3l.yggdrasil.persistence.agent;

import io.github.s3s3l.yggdrasil.persistence.snapshot.Snapshot;

/**
 * <p>
 * </p>
 * Date: Sep 16, 2019 2:37:33 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface SnapshotAgent<T, S extends Snapshot<T, K, V>, K, V> {

    S takeSnapshot(T data);

    T restore(S snapshot);

    boolean testData(T data);

    boolean testSnapshot(S snapshot);
}
