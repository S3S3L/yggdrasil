package org.s3s3l.yggdrasil.utils.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ClassName: SecurityUtils <br>
 * date: 2016年1月14日 上午10:46:41 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public abstract class SecurityUtils {

    private static final Pattern lowerCasePattern = Pattern.compile("[a-z]+");
    private static final Pattern upperCasePattern = Pattern.compile("[A-Z]+");
    private static final Pattern specialSymbolsPattern = Pattern.compile("[!@#$%^&*()_\\+\\-={}.\\[\\]:;\"'<\\?>,/]+");
    private static final Pattern numberPattern = Pattern.compile("\\d+");
    
    private static final String DIGIT = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    /**
     * 获取MD5编码
     * 
     * @param str
     *            源字符串
     * @return MD5编码后的字符串
     */
    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();

            int i;

            StringBuilder buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }
        return str;
    }

    /**
     * 获取指定编码
     * 
     * @param str
     *            源字符串
     * @param encName
     *            编码算法名称
     * @return 编码后的字符串
     */
    public static String encrypt(String str, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = str.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "SHA-256";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String encryptPassword(String password) {
        return encrypt(password, "SHA-1");
    }

    /**
     * 
     * 检查是否是弱密码
     * 
     * @author kehw_zwei
     * @param password
     *            密码
     * @return 密码长度小于6或不包含字母则返回true，否则返回false
     * @since JDK 1.8
     */
    public static boolean isWeakPassword(String password) {

        return StringUtils.isEmpty(password) || password.length() < 8 || !lowerCasePattern.matcher(password)
                .find() || !upperCasePattern.matcher(password)
                        .find()
                || !specialSymbolsPattern.matcher(password)
                        .find()
                || !numberPattern.matcher(password)
                        .find();
    }

    /**
     * 
     * To HexString
     * 
     * @author kehw_zwei
     * @param bts
     *            字节数组
     * @return Hex字符串
     * @since JDK 1.8
     */
    public static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * 
     * HexString To Bytes
     * 
     * @author kehw_zwei
     * @param str
     *            字符串
     * @return 字节数组
     * @since JDK 1.8
     */
    public static byte[] hex2Bytes(String str) {
        char[] ch = str.toLowerCase()
                .toCharArray();
        int len = ch.length;
        byte[] res = new byte[len >> 1];

        for (int i = 0, j = 0; j < len; i++, j += 2) {
            int f = toDigit(ch[j], 16) << 4;
            f = f | toDigit(ch[j + 1], 16);
            res[i] = (byte) (f & 0xFF);
        }

        return res;
    }

    /**
     * 
     * 将字符转化成十进制整数
     * 
     * @author kehw_zwei
     * @param ch
     *            字符
     * @param radix
     *            进制
     * @return 十进制数
     * @since JDK 1.8
     */
    private static int toDigit(char ch, int radix) {
        int digit = Character.digit(ch, radix);
        if (digit == -1) {
            throw new IllegalArgumentException(String.format("Invalid Character \"%s\"", ch));
        }

        return digit;
    }

    /**
     * 
     * 将字符串转化成十进制整数
     * 
     * @author kehw_zwei
     * @param str
     *            字符串
     * @param radix
     *            进制
     * @return 十进制数
     * @since JDK 1.8
     */
    public static BigInteger toDigit(String str, int radix) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        StringBuilder digitStr = new StringBuilder();
        for (char ch : str.toCharArray()) {
            digitStr.append(toDigit(ch, radix));
        }

        return new BigInteger(digitStr.toString());
    }

    /**
     * 
     * 转换十进制数为任意进制数的字符串（最大36进制） 递归
     * 
     * @author kehw_zwei
     * @param num
     *            十进制数
     * @param base
     *            要转换成的进制数
     * @return 指定进制的字符串(num为0时返回空字符串)
     * @since JDK 1.8
     */
    private static String baseString(int num, int base) {
        String str = "";
        if (num == 0) {
            return "";
        } else {
            str = baseString(num / base, base);
            return str + DIGIT.charAt(num % base);
        }
    }

    /**
     * 
     * 转换十进制数为任意进制数的字符串（最大36进制） 递归
     * 
     * @author kehw_zwei
     * @param num
     *            十进制数
     * @param base
     *            要转换成的进制数
     * @return 指定进制的字符串(num为0时返回空字符串)
     * @since JDK 1.8
     */
    private static String baseString(BigInteger num, int base) {
        String str = "";
        if (num.shortValue() == 0) {
            return "";
        } else {
            BigInteger valueOf = BigInteger.valueOf(base);
            str = baseString(num.divide(valueOf), base);
            return str + DIGIT.charAt(num.mod(valueOf)
                    .shortValue());
        }
    }

    /**
     * 
     * 转换十进制数为任意进制数的字符串（最大36进制） 递归
     * 
     * @author kehw_zwei
     * @param num
     *            十进制数
     * @param base
     *            要转换成的进制数
     * @return 指定进制的字符串(num为0时返回0)
     * @since JDK 1.8
     */
    public static String baseStringFixed(Object num, int base) {
        String str = "";
        if (num instanceof BigInteger) {
            str = baseString((BigInteger) num, base);
        } else if (num instanceof String) {
            str = baseString(new BigInteger((String) num), base);
        } else if (num instanceof Number) {
            str = baseString(((Number) num).intValue(), base);
        }
        return StringUtils.isEmpty(str) ? "0" : str;
    }

    /**
     * 
     * 转换十进制数为任意进制数({@code 36 - excludeWords.length})的字符串（最大36进制） 递归
     * 
     * @author kehw_zwei
     * @param num
     *            十进制数
     * @param base
     *            要转换成的进制数 < ({@code 36 - excludeWords.length})
     * @param excludeWords
     *            要排除的字母
     * @return 指定进制的字符串(num为0时返回空字符串)
     * @since JDK 1.8
     */
    public static String baseString(int num, int base, String... excludeWords) {
        String str = "";
        String excluedDigit = DIGIT;
        for (String excludeWord : excludeWords) {
            excluedDigit = excluedDigit.replaceAll(excludeWord, "");
        }
        if (num == 0) {
            return "";
        } else {
            str = baseString(num / base, base);
            return str + DIGIT.charAt(num % base);
        }
    }

    /**
     * 
     * 转换十进制数为任意进制数的字符串（最大36进制） 递归
     * 
     * @author kehw_zwei
     * @param num
     *            十进制数
     * @param base
     *            要转换成的进制数
     * @return 指定进制的字符串(num为0时返回0)
     * @since JDK 1.8
     */
    public static String baseStringFixed(Object num, int base, int minLength, char padChar) {
        return org.apache.commons.lang3.StringUtils.leftPad(baseStringFixed(num, base), minLength, padChar);
    }
}
