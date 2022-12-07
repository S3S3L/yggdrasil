package io.github.s3s3l.yggdrasil.bean;

/**
 * <p>
 * </p>
 * ClassName:EnumBoolean <br>
 * Date: Nov 15, 2016 11:19:11 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum EnumBoolean {
    TRUE(true, "是"), FALSE(false, "否");

    private boolean value;
    private String info;

    private EnumBoolean(boolean value, String info) {
        this.value = value;
        this.info = info;
    }

    public boolean value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }

    public static EnumBoolean parse(boolean value) {
        EnumBoolean[] enums = values();

        for (EnumBoolean currentEnum : enums) {
            if (currentEnum.value == value) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No matching constant for [%s]", value));
    }
}
