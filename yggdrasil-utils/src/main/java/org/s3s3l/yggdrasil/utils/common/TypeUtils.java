package org.s3s3l.yggdrasil.utils.common;

import java.nio.ByteBuffer;

/**
 * <p>
 * </p>
 * ClassName:TypeCastor <br>
 * Date: Dec 27, 2017 2:53:05 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class TypeUtils {

    public static <T> T fromString(String src, Class<T> type) {
        return type.cast(src);
    }

    public static byte[] longToBytes(long l) {
        ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
        buf.putLong(l);
        return buf.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
        buf.put(bytes);
        buf.flip();
        return buf.getLong();
    }
}
