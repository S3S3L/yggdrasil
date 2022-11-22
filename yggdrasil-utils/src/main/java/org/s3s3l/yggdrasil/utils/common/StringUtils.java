package org.s3s3l.yggdrasil.utils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.common.base.CaseFormat;

/**
 * 
 * ClassName: StringUtils <br>
 * date: 2016年1月14日 上午10:47:05 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public class StringUtils {

    public static final String DEFAULT_REDIS_KEY = "config:default";
    public static final String EMPTY_STRING = "";
    public static final String BASE64HEADER = "data:image/png;base64,";
    public static final String EMPTY_JSON_OBJECT = "{}";
    public static final String NULL_STRING = "null";
    public static final String NEW_LINE_STRING = System.getProperty("line.separator");
    public static final char UNDERLINE = '_';
    public static final Integer UUID_LENGTH = 32;
    private static final Pattern UUID_PATTERN = Pattern.compile("[a-zA-Z\\d]+");

    public static boolean isEmpty(String str) {
        return str == null || EMPTY_STRING.equals(str);
    }

    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            isEmpty((String) obj);
        }
        return obj == null;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean equals(String src, String target) {
        return src != null && target != null && src.equals(target);
    }

    /**
     * 生成不带'-'的uuid
     * 
     * @return uuid
     */
    public static String getUUIDNoLine() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString()
                .replaceAll("-", "");
    }

    /**
     * 
     * 截取字符串中首个匹配 "{@code [a-zA-Z\\d]+}" 的字符串
     * 
     * @param uuid
     * @return
     * @since JDK 1.8
     */
    public static String trimUUID(String uuid) {
        if (isEmpty(uuid)) {
            return EMPTY_STRING;
        }
        Matcher matcher = UUID_PATTERN.matcher(uuid);
        if (matcher.find()) {
            return matcher.toMatchResult()
                    .group();
        }

        return EMPTY_STRING;
    }

    /**
     * 
     * isMatch: <br>
     * 
     * @author kehw_zwei
     * @param src
     *              源字符串
     * @param regex
     *              正则
     * @return 如果匹配true，不匹配false
     * @since JDK 1.8
     */
    public static boolean isMatch(String src, String regex) {

        Pattern pattern = null;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            return false;
        }

        Matcher matcher = pattern.matcher(src);

        return matcher.find();
    }

    /**
     * 
     * 转化为Java字段命名格式
     * 
     * @author kehw_zwei
     * @param src
     *                  源字符串
     * @param srcFormat
     *                  源格式
     * @return Java字段命名格式字符串
     * @since JDK 1.8
     */
    public static String toJavaFieldName(String src, CaseFormat srcFormat) {
        return toFormatedString(src, srcFormat, CaseFormat.LOWER_CAMEL);
    }

    /**
     * 
     * 转化为指定格式的字符串
     * 
     * @author kehw_zwei
     * @param src
     *                     源字符串
     * @param srcFormat
     *                     源格式
     * @param targetFormat
     *                     目标格式
     * @return 目标格式字符串
     * @since JDK 1.8
     */
    public static String toFormatedString(String src, CaseFormat srcFormat, CaseFormat targetFormat) {
        return srcFormat.to(targetFormat, src);
    }

    /**
     * 
     * 将字符串首字母转化为大写
     * 
     * @author kehw_zwei
     * @param str
     *            源字符串
     * @return 首字母大写的字符串
     * @since JDK 1.8
     */
    public static String firstCharToUpperCase(String str) {
        if (isEmpty(str)) {
            return EMPTY_STRING;
        }

        return str.replaceFirst("\\.", String.valueOf(str.charAt(0))
                .toUpperCase());
    }

    /**
     * 
     * 将字符串首字母转化为小写
     * 
     * @author kehw_zwei
     * @param str
     *            源字符串
     * @return 首字母小写的字符串
     * @since JDK 1.8
     */
    public static String firstCharToLowerCase(String str) {
        if (isEmpty(str)) {
            return EMPTY_STRING;
        }

        return str.replaceFirst("\\.", String.valueOf(str.charAt(0))
                .toLowerCase());
    }

    // 驼峰命名转下划线命名

    /**
     *
     * 驼峰命名转下划线命名
     * 
     * @author carter_wang
     * @param str
     *            源字符串
     * @return 大写字母转换成下划线字母的字符串 eg. myJavaClass ---> my_java_class
     */
    public static String camelToUnderline(String str) {
        if (str == null || str.trim()
                .isEmpty()) {
            return "";
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            br.lines()
                    .forEach(sb::append);
        }

        return sb.toString();
    }
}
