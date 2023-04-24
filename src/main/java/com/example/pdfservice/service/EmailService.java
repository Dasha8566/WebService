package com.example.pdfservice.service;

import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.exceptions.DataBaseException;
import com.example.pdfservice.exceptions.EmailException;
import com.example.pdfservice.repo.CertificateRepository;
import com.example.pdfservice.repo.EventRepository;
import com.example.pdfservice.utils.EmailSender;
import com.example.pdfservice.utils.data.EmailAttachment;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailService {

    private EmailSender emailSender;
    private CertificateRepository certificateRepository;
    private PdfService pdfService;

    private EventRepository eventRepository;

    public void sendTestEmail(String recipient, String msgBody, String subject) throws EmailException {
        emailSender.sendSimpleMail(recipient, msgBody, subject);
    }

    public void sendEmailByCertificateId(@NonNull Long certId) throws DataBaseException, IOException, EmailException {
        Optional<Certificate> certificate = certificateRepository.findById(certId);
        if (certificate.isPresent()) {
            String recipient = certificate.get().getEmail();
            String msgBody = "Вітаємо, " + certificate.get().getFullName()
                    + "! Ви отримали сертифікат за участь у "
                    + certificate.get().getEvent().getEventName();
            String subject = "Сертифікат " + certificate.get().getEvent().getEventName();
            String fileName = certificate.get().getEvent().getEventName() + ".pdf";
            List<EmailAttachment> attachments = new ArrayList<>();
            EmailAttachment emailAttachment = new EmailAttachment(fileName, pdfService.getPdfByCertId(certId));
            attachments.add(emailAttachment);
            emailSender.sendAttachmentsMail(recipient, msgBody, subject, attachments);
        } else {
            throw new DataBaseException("Certificate with id=" + certId + " doesn't exist");
        }
    }

    public void sendEmailByEventId(@NonNull Long eventId) throws DataBaseException, EmailException, IOException {
        List<Certificate> certificates = certificateRepository.findAllByEvent(eventRepository.getById(eventId));
        if (!certificates.isEmpty()) {
            for (Certificate certificate : certificates) {
                String recipient = certificate.getEmail();
                String msgBody = "Вітаємо, " + certificate.getFullName()
                        + "! Ви отримали сертифікат за участь у "
                        + certificate.getEvent().getEventName();
                String subject = "Сертифікат " + certificate.getEvent().getEventName();
                String fileName = certificate.getEvent().getEventName() + ".pdf";
                List<EmailAttachment> attachments = new ArrayList<>();
                EmailAttachment emailAttachment = new EmailAttachment(fileName, pdfService.getPdfByCertId(certificate.getId()));
                attachments.add(emailAttachment);
                emailSender.sendAttachmentsMail(recipient, msgBody, subject, attachments);

            }


        } else{
                throw new DataBaseException("Event with id=" + eventId + " doesn't exist");
            }
        }
    }

