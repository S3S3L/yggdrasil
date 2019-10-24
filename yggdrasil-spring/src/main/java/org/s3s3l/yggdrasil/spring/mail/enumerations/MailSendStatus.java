package org.s3s3l.yggdrasil.spring.mail.enumerations;

/**
 * ClassName:MailSendStatus <br>
 * Date: 2016年2月14日 下午4:43:06 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum MailSendStatus {
    MESSAGE_ERROR(0, "邮件信息错误，请联系管理员！"),
    TEMPLATE_NOT_FOUND(0, "未找到邮件模板"),
    WRONG_ADDRESS(0, "错误的邮件地址！"),
    FAIL(0, "邮件发送失败，请稍后再试！"),
    SUCCESS(1, "邮件发送成功！");

    private int value;
    private String info;

    private MailSendStatus(int value, String info) {
        this.value = value;
        this.info = info;
    }

    public int value() {
        return this.value;
    }

    public String info() {
        return this.info;
    }

    public static MailSendStatus parse(int value) {
        MailSendStatus[] enums = values();

        for (MailSendStatus currentEnum : enums) {
            if (currentEnum.value == value) {
                return currentEnum;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
