package org.s3s3l.yggdrasil.utils.es.enumerations;

/**
 * <p>
 * </p>
 * ClassName:EnumQueryType <br>
 * Date: Jan 2, 2019 4:12:33 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum QueryType {
    UNKNOWN(-99, "未知"), SEARCH(1, "_search"), UPDATE(2, "_update");

    private int value;
    private String info;

    private QueryType(int value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }

    public static QueryType parse(int value) {
        QueryType[] enums = values();

        for (QueryType currentEnum : enums) {
            if (currentEnum.value == value) {
                return currentEnum;
            }
        }
        return UNKNOWN;
    }
}
