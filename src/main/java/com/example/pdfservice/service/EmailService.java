package com.example.pdfservice.service;

import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.entity.User;
import com.example.pdfservice.enums.UserPermission;
import com.example.pdfservice.exceptions.DataBaseException;
import com.example.pdfservice.exceptions.EmailException;
import com.example.pdfservice.repo.CertificateRepository;
import com.example.pdfservice.repo.EventRepository;
import com.example.pdfservice.repo.UserRepository;
import com.example.pdfservice.utils.CalendarUtil;
import com.example.pdfservice.utils.EmailSender;
import com.example.pdfservice.utils.data.EmailAttachment;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailService {

    private EmailSender emailSender;
    private CertificateRepository certificateRepository;
    private PdfService pdfService;

    private EventRepository eventRepository;

    private UserRepository userRepository;

    private EventService eventService;

    public void sendTestEmail(String recipient, String msgBody, String subject) throws EmailException {
        emailSender.sendSimpleMail(recipient, msgBody, subject);
    }

    public void sendEmailByCertificateId(@NonNull Long certId, String userEmail) throws DataBaseException, IOException, EmailException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));




        Optional<Certificate> certificate = certificateRepository.findById(certId);

        if (certificate.isPresent()) {
        if (user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                || certificate.get().getEvent().getUsers().contains(user)) {


            String recipient = certificate.get().getEmail();
            String msgBody = "Вітаємо, " + certificate.get().getFullName()
                    + "! Ви отримали сертифікат за участь у "
                    + certificate.get().getEvent().getEventName();
            String subject = "Сертифікат " + certificate.get().getEvent().getEventName();
            String fileName = certificate.get().getEvent().getEventName() + ".pdf";
            List<EmailAttachment> attachments = new ArrayList<>();
            EmailAttachment emailAttachment = new EmailAttachment(fileName, pdfService.getPdfByCertId(certId, userEmail));
            attachments.add(emailAttachment);
            emailSender.sendAttachmentsMail(recipient, msgBody, subject, attachments);
        }else {
            throw new AccessException("You don't have an access to this certificate with id=" + certId);
        }
        } else {
            throw new DataBaseException("Certificate with id=" + certId + " doesn't exist");
        }
    }

    public void sendEmailByEventId(@NonNull Long eventId, String userEmail, Boolean resend) throws DataBaseException, EmailException, IOException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));


        Event event = eventRepository.findById(eventId).orElseThrow(()->new DataBaseException("Event with id="+eventId+" doesn't exist"));



        if(user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                || event.getUsers().contains(user)) {
            List<Certificate> certificates = certificateRepository.findAllByEvent(event);
            if (!certificates.isEmpty()) {
                for (Certificate certificate : certificates) {

                    if(resend||(certificate.getLastSendDate())==null){

                    String recipient = certificate.getEmail();
                    String msgBody = "Вітаємо, " + certificate.getFullName()
                            + "! Ви отримали сертифікат за участь у "
                            + certificate.getEvent().getEventName();
                    String subject = "Сертифікат " + certificate.getEvent().getEventName();
                    String fileName = certificate.getEvent().getEventName() + ".pdf";
                    List<EmailAttachment> attachments = new ArrayList<>();
                    EmailAttachment emailAttachment = new EmailAttachment(fileName, pdfService.getPdfByCertId(certificate.getId(), userEmail));
                    attachments.add(emailAttachment);
                    emailSender.sendAttachmentsMail(recipient, msgBody, subject, attachments);
                    certificate.setLastSendDate(Calendar.getInstance());
                    certificateRepository.save(certificate);

                }
            }
            }else {
                throw new DataBaseException("Certificates don't exist for this event with id=" + eventId );
            }
        }else{
            throw new AccessException("You don't have an access to this event with id="+eventId);
        }
    }



    }

