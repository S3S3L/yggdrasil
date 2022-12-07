package io.github.s3s3l.yggdrasil.utils.security;

/**
 * ClassName:Encipher <br>
 * Date: 2016年3月15日 下午6:48:35 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Encipher {

    /**
     * 
     * 加密字符串
     * 
     * @param src
     *            源字符串
     * @return 加密后的字符串
     * @since JDK 1.8
     */
    String encrypt(String src);

    /**
     * 
     * 解密字符串
     * 
     * @param src
     *            源字符串
     * @return 解密后的字符串
     * @since JDK 1.8
     */
    String decrypt(String src);
}
