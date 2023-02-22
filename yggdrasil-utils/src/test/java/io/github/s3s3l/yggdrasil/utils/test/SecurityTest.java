package io.github.s3s3l.yggdrasil.utils.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.utils.security.SecurityUtils;

/**
 * <p>
 * </p>
 * ClassName:SecurityTest <br>
 * Date: May 23, 2017 2:40:11 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class SecurityTest {

    @Test
    public void weekPasswordTest() {
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123+456Aa"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("a123+456A"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123a+456A"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("A123a+456"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123Aa+456"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("+123456Aa"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa+"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa["));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa]"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa{"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa}"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa;"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa:"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa\""));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa'"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa,"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa<"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa."));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa>"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa/"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa?"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa!"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa@"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa#"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa$"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa%"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa^"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa&"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa*"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa("));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa)"));
        Assertions.assertFalse(SecurityUtils.isWeakPassword("123456Aa-"));

        Assertions.assertTrue(SecurityUtils.isWeakPassword(""));
        Assertions.assertTrue(SecurityUtils.isWeakPassword("123456Aa"));
        Assertions.assertTrue(SecurityUtils.isWeakPassword("123456A-"));
        Assertions.assertTrue(SecurityUtils.isWeakPassword("123456a-"));
        Assertions.assertTrue(SecurityUtils.isWeakPassword("1234Aa-"));
        Assertions.assertTrue(SecurityUtils.isWeakPassword("Abcdefa-"));
    }
}
