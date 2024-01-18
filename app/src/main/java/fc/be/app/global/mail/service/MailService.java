package fc.be.app.global.mail.service;

import fc.be.app.global.exception.InternalServiceErrorCode;
import fc.be.app.global.exception.InternalServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("admin@tripvote.site");
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendVerificationCode(String to, String subject, String verificationCode) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("donghar2004@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            // Load HTML template from resources
            Resource resource = resourceLoader.getResource("classpath:/templates/mail-template.html");
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            String htmlTemplate = FileCopyUtils.copyToString(reader);

            // Replace template variable with verification code
            String htmlContent = htmlTemplate.replace("{{verificationCode}}", verificationCode);

            // Set HTML content to MimeMessage
            helper.setText(htmlContent, true);

            // Send the email
            javaMailSender.send(message);

        } catch (MessagingException | IOException e) {
            // Handle exceptions
            throw new InternalServiceException(InternalServiceErrorCode.NO_SUCH_FILE, e);
        }
    }
}
