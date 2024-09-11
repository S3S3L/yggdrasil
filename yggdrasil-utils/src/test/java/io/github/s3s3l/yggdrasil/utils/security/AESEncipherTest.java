package io.github.s3s3l.yggdrasil.utils.security;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;

import io.github.s3s3l.yggdrasil.utils.security.impl.AESEncipher;

public class AESEncipherTest extends BaseEncipherTest {

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        encipher = new AESEncipher();
        encipherWithKey = new AESEncipher(keyBytes);
    }
}
