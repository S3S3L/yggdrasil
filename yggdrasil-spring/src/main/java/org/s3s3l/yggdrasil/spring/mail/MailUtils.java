package org.s3s3l.yggdrasil.spring.mail;

import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.s3s3l.yggdrasil.spring.mail.enumerations.MailSendStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;

/**
 * ClassName:MailUtils <br>
 * Date: 2016年2月14日 下午4:41:25 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class MailUtils {

    private JavaMailSender mailSender;

    public MailUtils(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public MailSendStatus sendMail(String subject, Object body, String from, String... tos) {
        MailSendStatus result = MailSendStatus.SUCCESS;

        try {
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            message.setContent(body, "text/html;charset=utf-8");
            message.setFrom(from);
            message.setSubject(subject);
            message.setRecipients(RecipientType.TO,
                    InternetAddress.parse(StringUtils.arrayToCommaDelimitedString(tos)));

            mailSender.send(message);
        } catch (Exception e) {
            throw new MailSendException(e);
        }

        return result;
    }
}
