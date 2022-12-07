package io.github.s3s3l.yggdrasil.utils.verify;

/**
 * <p>
 * </p>
 * ClassName:Verifier <br>
 * Date: Apr 7, 2017 4:36:14 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Verifier {

    void tryVerify(Object param, Class<?> type);

    <T> void verify(T param, Class<T> type);

    <T> void verify(T param, Class<T> type, String scope);
}
