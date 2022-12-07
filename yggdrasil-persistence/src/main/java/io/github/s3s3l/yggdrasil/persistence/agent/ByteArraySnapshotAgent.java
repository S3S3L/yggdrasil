package io.github.s3s3l.yggdrasil.persistence.agent;

import io.github.s3s3l.yggdrasil.persistence.snapshot.ByteArraySnapshot;

/**
 * <p>
 * </p>
 * Date: Sep 16, 2019 2:40:49 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ByteArraySnapshotAgent<T, S extends ByteArraySnapshot<T>> extends SnapshotAgent<T, S, byte[], byte[]> {

}
