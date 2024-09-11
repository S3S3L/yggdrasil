package io.github.s3s3l.yggdrasil.utils.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class BaseEncipherTest {
    
    protected Encipher encipher;
    protected Encipher encipherWithKey;
    protected final byte[] keyBytes = new byte[32]; // 256 bits key
    private final String plainText = "test";

    @Test
    public void testEncryptDecryptWithGeneratedKey() throws Exception {
        String encrypted = encipher.encrypt(plainText);
        String decrypted = encipher.decrypt(encrypted);
        Assertions.assertEquals(plainText, decrypted);
    }

    @Test
    public void testEncryptDecryptWithProvidedKey() throws Exception {
        String encrypted = encipherWithKey.encrypt(plainText);
        String decrypted = encipherWithKey.decrypt(encrypted);
        Assertions.assertEquals(plainText, decrypted);
    }

    @Test
    public void testEncryptNullInput() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> encipher.encrypt(null));
    }

    @Test
    public void testDecryptNullInput() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> encipher.decrypt(null));
    }
}
