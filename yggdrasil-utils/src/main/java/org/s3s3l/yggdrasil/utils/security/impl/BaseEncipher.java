package org.s3s3l.yggdrasil.utils.security.impl;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.s3s3l.yggdrasil.utils.security.Encipher;
import org.s3s3l.yggdrasil.utils.security.SecurityUtils;

/**
 * ClassName:BaseEncipher <br>
 * Date: 2016年3月16日 下午5:58:21 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class BaseEncipher implements Encipher {

    protected SecretKey secretKey = null;
    protected Cipher cipher;
    protected Integer keySize;
    protected String key;

    public BaseEncipher(final String ALGORITHM) {
        try {
            if (secretKey == null || secretKey.isDestroyed()) {
                KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
                secretKey = keygen.generateKey();
            }
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public BaseEncipher(final String ALGORITHM, byte[] keyBytes) {

        try {
            if (this.secretKey == null || this.secretKey.isDestroyed()) {
                this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            }
            this.cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Integer getKeySize() {
        return keySize;
    }

    public void setKeySize(Integer keySize) {
        this.keySize = keySize;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public Cipher getCipher() {
        return cipher;
    }

    public void setCipher(Cipher cipher) {
        this.cipher = cipher;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(String src) {

        if (secretKey == null || secretKey.isDestroyed()) {
            throw new IllegalArgumentException("SecretKey not set.");
        }

        byte[] result;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] srcBytes = src.getBytes(StandardCharsets.UTF_8);
            result = cipher.doFinal(srcBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException(e);
        }
        return SecurityUtils.bytes2Hex(result);
    }

    @Override
    public String decrypt(String src) {

        if (secretKey == null || secretKey.isDestroyed()) {
            throw new IllegalArgumentException("SecretKey not set.");
        }

        byte[] result;

        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] srcBytes = SecurityUtils.hex2Bytes(src);
            result = cipher.doFinal(srcBytes);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException(e);
        }

        return new String(result, StandardCharsets.UTF_8);
    }

}
