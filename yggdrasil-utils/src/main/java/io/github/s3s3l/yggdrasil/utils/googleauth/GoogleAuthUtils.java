package io.github.s3s3l.yggdrasil.utils.googleauth;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import io.github.s3s3l.yggdrasil.utils.common.BarcodeUtils;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.file.FileFormat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.GoogleAuthenticator;

/**
 * googleauth 工具类 <br>
 * ClassName: GoogleauthUtils <br>
 * date: 2015年12月21日 上午11:47:15 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public class GoogleAuthUtils {

    public static boolean auth(String secret, String codeStr) {
        int code = Integer.parseInt(codeStr);
        return new GoogleAuthenticator().authorize(secret, code);
    }

    /**
     * 以用户名为种子获取secretKey
     * 
     * @author kehw_zwei
     * @param userName
     *            用户名
     * @return secretKey Base32
     * @since JDK 1.8
     */
    public static String getSecretKey(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return StringUtils.EMPTY_STRING;
        }
        return new GoogleAuthenticator().createCredentials(userName)
                .getKey();
    }

    /**
     * 
     * 生成指定大小的用于扫描的二维码
     * 
     * @author kehw_zwei
     * @param userName
     *            用户名
     * @param secretKey
     *            secretKey
     * @param width
     *            宽度 px
     * @param height
     *            高度 px
     * @return 二维码 Base64
     * @throws WriterException
     *             A base class which covers the range of exceptions which may
     *             occur when encoding a barcode using the Writer framework.
     * @throws IOException
     *             Signals that an I/O exception of some sort has occurred. This
     *             class is the general class of exceptions produced by failed
     *             or interrupted I/O operations.
     * @since JDK 1.8
     */
    public static String getQrcodeBase64(String userName, String secretKey, int width, int height)
            throws WriterException,
            IOException {

        StringBuilder sb = new StringBuilder("otpauth://totp/").append(userName)
                .append("?secret=")
                .append(secretKey);
        BufferedImage imgBuffer = BarcodeUtils.getBarcodeBufferedStream(sb.toString(), width, height,
                StandardCharsets.UTF_8, BarcodeFormat.QR_CODE);

        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        ImageIO.write(imgBuffer, FileFormat.JPG.name(), buff);

        StringBuilder base64String = new StringBuilder(StringUtils.BASE64HEADER)
                .append(Base64.encodeBase64String(buff.toByteArray()));
        return base64String.toString();
    }

    public static byte[] base64ToImage(String base64String) {
        return Base64.decodeBase64(base64String.replaceFirst(StringUtils.BASE64HEADER, ""));
    }
}
