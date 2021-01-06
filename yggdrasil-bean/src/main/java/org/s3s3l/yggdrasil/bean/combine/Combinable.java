package org.s3s3l.yggdrasil.bean.combine;

/**
 * <p>
 * </p>
 * ClassName:Combinable <br>
 * Date: Mar 21, 2017 5:37:19 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Combinable<T> {

    Combinable<T> combine(T data);

    Combinable<T> combine(Combinable<T> combine);

    T get();

    void set(T data);
}
