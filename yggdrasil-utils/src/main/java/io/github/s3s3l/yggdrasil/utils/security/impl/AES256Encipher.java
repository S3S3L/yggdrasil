package io.github.s3s3l.yggdrasil.utils.security.impl;

public class AES256Encipher extends BaseEncipher {

    private static final String ALGORITHM = "AES";

    public AES256Encipher() {
        super(ALGORITHM, 256);
    }

    public AES256Encipher(byte[] keyBytes) {
        super(ALGORITHM, keyBytes);
    }
}
