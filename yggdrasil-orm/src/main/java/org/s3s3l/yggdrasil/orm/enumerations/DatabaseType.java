package org.s3s3l.yggdrasil.orm.enumerations;

/**
 * 
 * <p>
 * </p>
 * ClassName: DatabaseType <br>
 * date: Sep 20, 2019 11:30:16 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum DatabaseType {
    MYSQL(1, "MySql"), ORACLE(2, "Oracle"), POSTGRESQL(3, "Postgresql"), SQLSERVER(4, "SqlServer");

    private int value;
    private String info;

    private DatabaseType(int value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }

    public static DatabaseType parse(int value) {
        DatabaseType[] enums = values();

        for (DatabaseType currentEnum : enums) {
            if (currentEnum.value == value) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No matching constant for [%s]", value));
    }

    public static DatabaseType parse(String info) {
        DatabaseType[] enums = values();

        for (DatabaseType currentEnum : enums) {
            if (currentEnum.info.equalsIgnoreCase(info)) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No matching constant for [%s]", info));
    }
}
