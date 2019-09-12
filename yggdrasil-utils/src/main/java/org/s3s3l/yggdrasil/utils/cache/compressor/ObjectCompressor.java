package org.s3s3l.yggdrasil.utils.cache.compressor;

/**
 * <p>
 * </p>
 * ClassName:ObjectCompressor <br>
 * Date: Apr 4, 2019 2:18:17 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ObjectCompressor<T> {

    byte[] compress(T obj);

    T decompress(byte[] bytes);
}
