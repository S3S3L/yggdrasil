package org.s3s3l.yggdrasil.persistence;

/**
 * <p>
 * </p>
 * Date: Sep 17, 2019 3:59:59 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Persistence<T> {

    void save(T data);

    T load();
}
