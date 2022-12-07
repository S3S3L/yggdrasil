package io.github.s3s3l.yggdrasil.document.excel.enumerations;

/**
 * <p>
 * </p>
 * ClassName:WorkbookType <br>
 * Date: Nov 14, 2016 5:33:31 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum WorkbookType {
    XLS(1, ".xls"), XLSX(2, ".xlsx");

    private int value;
    private String info;

    private WorkbookType(int value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }

    public static WorkbookType parse(int value) {
        WorkbookType[] enums = values();

        for (WorkbookType currentEnum : enums) {
            if (currentEnum.value == value) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No matching constant for [%s]", value));
    }

    public static WorkbookType parse(String info) {
        WorkbookType[] enums = values();

        for (WorkbookType currentEnum : enums) {
            if (currentEnum.info.equals(info)) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No matching constant for [%s]", info));
    }
}
