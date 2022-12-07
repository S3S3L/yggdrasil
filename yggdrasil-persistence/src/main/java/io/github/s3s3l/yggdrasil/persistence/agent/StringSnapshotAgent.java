package io.github.s3s3l.yggdrasil.persistence.agent;

import io.github.s3s3l.yggdrasil.persistence.snapshot.StringSnapshot;

/**
 * <p>
 * </p>
 * Date: Sep 16, 2019 2:39:41 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface StringSnapshotAgent<T, S extends StringSnapshot<T>> extends SnapshotAgent<T, S, String, String> {

}
