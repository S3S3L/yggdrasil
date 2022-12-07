package io.github.s3s3l.yggdrasil.utils.collection;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * <p>
 * </p>
 * ClassName:ArrayHelper <br>
 * Date: May 17, 2019 5:13:31 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class ArrayHelper {

    public static byte[] prefixAndJoin(byte[] delimiter, byte[] prefix, byte[]... params) {
        int length = Arrays.stream(params)
                .mapToInt(param -> param.length)
                .sum() + delimiter.length * params.length + prefix.length;
        ByteBuffer buf = ByteBuffer.allocate(length);
        if (prefix.length > 0) {
            buf.put(prefix);
            buf.put(delimiter);
        }
        if (params.length > 0) {
            for (int i = 0; i < params.length - 1; i++) {
                buf.put(params[i]);
                buf.put(delimiter);
            }

            buf.put(params[params.length - 1]);
        }
        buf.flip();
        return buf.array();
    }

    public static byte[] join(byte[] delimiter, byte[]... params) {
        return prefixAndJoin(delimiter, new byte[] {}, params);
    }

    public static byte[] prefix(byte[] delimiter, byte[] prefix) {
        return prefixAndJoin(delimiter, prefix);
    }
}
