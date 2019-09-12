package org.s3s3l.yggdrasil.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Assert;
import org.junit.Test;
import org.s3s3l.yggdrasil.utils.security.impl.AESEncipher;
import org.s3s3l.yggdrasil.utils.security.impl.BaseEncipher;

/**
 * <p>
 * </p>
 * ClassName:EncipherTest <br>
 * Date: Mar 14, 2018 1:32:04 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class EncipherTest {

    @Test
    public void baseTest() throws InvalidKeyException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException {
        String src = "hello world";
        BaseEncipher encipher = new AESEncipher();
        String encryptStr = encipher.encrypt(src);

        byte[] keyBytes = encipher.getSecretKey()
                .getEncoded();

        Assert.assertEquals(src, new AESEncipher(keyBytes).decrypt(encryptStr));
    }
}
