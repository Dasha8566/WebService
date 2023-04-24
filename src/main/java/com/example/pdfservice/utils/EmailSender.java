package com.example.pdfservice.utils;

import com.example.pdfservice.exceptions.EmailException;
import com.example.pdfservice.utils.data.EmailAttachment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EmailSender {

    CompletableFuture<Boolean> sendSimpleMail(
            String recipient,
            String msgBody,
            String subject
    ) throws EmailException;

    CompletableFuture<Boolean> sendAttachmentsMail(
            String recipient,
            String msgBody,
            String subject,
            List<EmailAttachment> attachments
    ) throws EmailException;
}
