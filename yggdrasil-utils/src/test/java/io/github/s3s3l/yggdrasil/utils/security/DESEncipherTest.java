package io.github.s3s3l.yggdrasil.utils.security;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;

import io.github.s3s3l.yggdrasil.utils.security.impl.DESEncipher;

public class DESEncipherTest extends BaseEncipherTest {
    private final byte[] keyBytes = new byte[8]; // 64 bits key

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        encipher = new DESEncipher();
        encipherWithKey = new DESEncipher(keyBytes);
    }
}
