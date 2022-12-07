package io.github.s3s3l.yggdrasil.utils.test;

import org.junit.Assert;
import org.junit.Test;
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
        Assert.assertFalse(SecurityUtils.isWeakPassword("123+456Aa"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("a123+456A"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123a+456A"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("A123a+456"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123Aa+456"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("+123456Aa"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa+"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa["));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa]"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa{"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa}"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa;"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa:"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa\""));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa'"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa,"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa<"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa."));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa>"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa/"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa?"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa!"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa@"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa#"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa$"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa%"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa^"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa&"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa*"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa("));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa)"));
        Assert.assertFalse(SecurityUtils.isWeakPassword("123456Aa-"));

        Assert.assertTrue(SecurityUtils.isWeakPassword(""));
        Assert.assertTrue(SecurityUtils.isWeakPassword("123456Aa"));
        Assert.assertTrue(SecurityUtils.isWeakPassword("123456A-"));
        Assert.assertTrue(SecurityUtils.isWeakPassword("123456a-"));
        Assert.assertTrue(SecurityUtils.isWeakPassword("1234Aa-"));
        Assert.assertTrue(SecurityUtils.isWeakPassword("Abcdefa-"));
    }
}
