package io.github.s3s3l.yggdrasil.utils.security.impl;

/**
 * ClassName:AESEncipher <br>
 * Date: 2016年3月16日 下午3:54:40 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class AESEncipher extends BaseEncipher {

    private static final String ALGORITHM = "AES";

    public AESEncipher() {
        super(ALGORITHM);
    }

    public AESEncipher(byte[] keyBytes) {
        super(ALGORITHM, keyBytes);
    }
}
