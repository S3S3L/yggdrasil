package org.s3s3l.yggdrasil.utils.log.enumerations;

/**
 * ClassName:EnumLogger <br>
 * Date: 2016年4月25日 下午4:49:08 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum LoggerType {
    SLF4J(0, "slf4j"), LOG4J(1, "log4j"), LOG4J2(2, "log4j2");

    private int value;
    private String info;

    private LoggerType(int value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }

    public static LoggerType parse(int value) {
        LoggerType[] enums = values();

        for (LoggerType currentEnum : enums) {
            if (currentEnum.value == value) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }

    public static LoggerType parse(String info) {
        LoggerType[] enums = values();

        for (LoggerType currentEnum : enums) {
            if (currentEnum.info.equalsIgnoreCase(info)) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + info + "]");
    }
}
