package org.s3s3l.yggdrasil.persistence.storage;

import org.s3s3l.yggdrasil.persistence.snapshot.Snapshot;

/**
 * <p>
 * </p>
 * Date: Sep 16, 2019 2:15:45 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Storage<T, K, V> {

    void store(Snapshot<T, K, V> snapshot);

    Snapshot<T, K, V> load(String id, String version);
}
