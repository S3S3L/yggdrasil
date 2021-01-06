
package org.s3s3l.yggdrasil.persistence.snapshot;

import java.util.Map;

import org.s3s3l.yggdrasil.persistence.Versioning;

/**
 * <p>
 * </p>
 * Date: Sep 16, 2019 2:17:41 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Snapshot<T, K, V> extends Versioning {

    T restore();

    Map<K, V> toMap();
}
