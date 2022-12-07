package io.github.s3s3l.yggdrasil.utils.common;

/**
 * <p>
 * </p>
 * ClassName:Utils <br>
 * Date: Aug 28, 2019 11:20:32 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class Utils {

    public static final int powerOfTwoFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        if (n < 0) {
            return 1;
        }
        return n >= Integer.MAX_VALUE ? Integer.MAX_VALUE : n + 1;
    }

    public static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
