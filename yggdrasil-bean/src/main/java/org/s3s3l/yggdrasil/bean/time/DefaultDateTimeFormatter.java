package org.s3s3l.yggdrasil.bean.time;

import java.time.format.DateTimeFormatter;

/**
 * <p>
 * </p>
 * ClassName:DefaultDateTimeFormatter <br>
 * Date: Sep 1, 2016 4:19:16 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultDateTimeFormatter {

	public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
}
