package io.github.s3s3l.yggdrasil.spring.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import io.github.s3s3l.yggdrasil.spring.mail.enumerations.MailSendStatus;
import jakarta.mail.internet.MimeMessage;

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

    public MailSendStatus sendMail(String subject, String body, String from, String... tos) {
        MailSendStatus result = MailSendStatus.SUCCESS;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setText(body, true);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setTo(tos);

            mailSender.send(message);
        } catch (Exception e) {
            throw new MailSendException(e);
        }

        return result;
    }
}
