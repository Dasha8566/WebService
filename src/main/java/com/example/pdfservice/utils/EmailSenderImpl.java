package com.example.pdfservice.utils;

import com.example.pdfservice.exceptions.EmailException;
import com.example.pdfservice.utils.data.EmailAttachment;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Component
@AllArgsConstructor
public class EmailSenderImpl implements EmailSender{

    private JavaMailSender javaMailSender;
    @Override
    @Async
    public CompletableFuture<Boolean> sendSimpleMail(
            String recipient,
            String msgBody,
            String subject
    ) throws EmailException {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        try {
            //mailMessage.setFrom("Сертифікат");
            mailMessage.setTo(recipient);
            mailMessage.setText(msgBody);
            mailMessage.setSubject(subject);
            // Sending the mail
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            throw new EmailException(e.getMessage());
        }
        return completedFuture(true);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> sendAttachmentsMail(
            String recipient,
            String msgBody,
            String subject,
            List<EmailAttachment> attachments
    ) throws EmailException {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(msgBody, true);
            if (attachments != null && attachments.size() > 0) {
                for (EmailAttachment attachment : attachments) {
                    helper.addAttachment(attachment.getFileName(), new ByteArrayResource(attachment.getData()));
                }
            }
            javaMailSender.send(message);
        }catch (Exception e){
            throw new EmailException(e.getMessage());
        }
        return completedFuture(true);
    }
}