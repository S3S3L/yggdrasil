package io.github.s3s3l.yggdrasil.persistence.storage;

import io.github.s3s3l.yggdrasil.persistence.snapshot.Snapshot;

/**
 * <p>
 * </p>
 * Date: Sep 19, 2019 3:28:44 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class BasicFileStorage<T> implements ByteArrayStorage<T> {

    @Override
    public void store(Snapshot<T, byte[], byte[]> snapshot) {
        // TODO store
    }

    @Override
    public Snapshot<T, byte[], byte[]> load(String id, String version) {
        return null;
    }

}
