package org.s3s3l.yggdrasil.utils.security.impl;

/**
 * ClassName:DESEncipher <br>
 * Date: 2016年3月17日 上午10:50:16 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DESEncipher extends BaseEncipher {

    private static final String ALGORITHM = "DES";

    public DESEncipher() {
        super(ALGORITHM);
    }

    public DESEncipher(byte[] keyBytes) {
        super(ALGORITHM, keyBytes);
    }
}
