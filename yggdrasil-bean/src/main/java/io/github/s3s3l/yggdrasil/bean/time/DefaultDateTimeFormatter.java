package io.github.s3s3l.yggdrasil.bean.time;

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
public abstract class DefaultDateTimeFormatter {
	private DefaultDateTimeFormatter() {
	}

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String TIME_PATTERN = "HH:mm:ss";

	public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

	public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern(DATE_PATTERN);

	public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern(TIME_PATTERN);
}
