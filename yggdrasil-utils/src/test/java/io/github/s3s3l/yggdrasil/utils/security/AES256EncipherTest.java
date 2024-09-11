package io.github.s3s3l.yggdrasil.utils.security;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;

import io.github.s3s3l.yggdrasil.utils.security.impl.AES256Encipher;

public class AES256EncipherTest extends BaseEncipherTest {

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        encipher = new AES256Encipher();
        encipherWithKey = new AES256Encipher(keyBytes);
    }
}