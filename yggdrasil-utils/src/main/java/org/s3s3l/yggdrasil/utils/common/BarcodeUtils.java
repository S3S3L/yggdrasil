package org.s3s3l.yggdrasil.utils.common;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.s3s3l.yggdrasil.utils.file.FileFormat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 条形码/二维码工具类 <br>
 * ClassName: BarcodeUtils <br>
 * date: 2015年12月21日 上午11:51:53 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public class BarcodeUtils {

	public static BufferedImage getBarcodeBufferedStream(String text, int width, int height, Charset charset,
			BarcodeFormat barcodeFormat) throws WriterException {
		Map<EncodeHintType, String> hints = new EnumMap<>(EncodeHintType.class);
		hints.put(EncodeHintType.CHARACTER_SET, charset.name());
		BitMatrix bitmatrix = new MultiFormatWriter().encode(text, barcodeFormat, width, height, hints);

		return toBufferedImage(bitmatrix);
	}

	public static boolean barcodeToFile(String text, int width, int height, Charset charset,
			BarcodeFormat barcodeFormat, String filePath, FileFormat fileFormat) throws WriterException, IOException {
		File outputFile = new File(filePath);
		if (outputFile.isDirectory()) {
			return false;
		}

		if (outputFile.exists()) {
			Files.delete(outputFile.toPath());
		}

		BufferedImage imgBuffer = getBarcodeBufferedStream(text, width, height, charset, barcodeFormat);

		ImageIO.write(imgBuffer, fileFormat.name(), outputFile);

		return true;
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
			}
		}
		return image;
	}
}
