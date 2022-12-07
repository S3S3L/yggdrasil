package io.github.s3s3l.yggdrasil.bean;

import java.util.Arrays;

/**
 * <p>
 * </p>
 * ClassName:FileType <br>
 * Date: Aug 19, 2016 10:10:48 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum FileType {
	PROPERTIES(1, new String[] { "properties" }),
	YAML(2, new String[] { "yaml", "yml" }),
	XML(3, new String[] { "xml" }),
	JSON(4, new String[] { "json" }),
    CLASS(4, new String[] { "class" });

	private int value;
	private String[] extensions;

	private FileType(int value, String[] extensions) {
		this.value = value;
		this.extensions = extensions;
	}

	public int value() {
		return this.value;
	}

	public String info() {
		return String.join(",", this.extensions);
	}

	public static FileType parse(int value) {
		FileType[] enums = values();

		for (FileType currentEnum : enums) {
			if (currentEnum.value == value) {
				return currentEnum;
			}
		}
		throw new IllegalArgumentException(String.format("No matching constant for [%s]", value));
	}

	public static FileType parse(String extendsion) {
		FileType[] enums = values();

		for (FileType currentEnum : enums) {
			if (Arrays.asList(currentEnum.extensions).contains(extendsion)) {
				return currentEnum;
			}
		}
		throw new IllegalArgumentException(String.format("No matching constant for [%s]", extendsion));
	}
}
