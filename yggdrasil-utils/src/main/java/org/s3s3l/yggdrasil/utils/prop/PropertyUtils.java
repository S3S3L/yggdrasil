package org.s3s3l.yggdrasil.utils.prop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * ClassName: PropertyUtils <br>
 * date: 2016年1月14日 上午10:50:16 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public abstract class PropertyUtils {

	/**
	 * 
	 * 获取配置
	 * 
	 * @author kehw_zwei
	 * @param configFilePath
	 *            配置文件路径
	 * @return Properties对象
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 * @since JDK 1.8
	 */
	public static Properties getConfig(String configFilePath) throws IOException {

		Properties config = null;
		InputStream inputStream;
		config = new Properties();
		inputStream = new FileInputStream(configFilePath);
		config.load(inputStream);
		inputStream.close();
		return config;
	}
}
