package com.example.pdfservice.api;

import com.example.pdfservice.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/email-management")
@AllArgsConstructor
public class EmailController {

    private EmailService emailService;

    @PostMapping("send-simple-test")
    public ResponseEntity<Boolean> sendTestSimpleMail(@RequestParam String recipient,@RequestParam String msgBody,@RequestParam String subject){
        try{
            emailService.sendTestEmail(recipient, msgBody, subject);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("send-by-certificate-id")
    public ResponseEntity<?> setEmailByCertificateId(@RequestParam Long certificateId){
        try{
            emailService.sendEmailByCertificateId(certificateId);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("send-by-event-id")
    public ResponseEntity<?> setEmailByEventId(@RequestParam Long eventId){
        try{
            emailService.sendEmailByEventId(eventId);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
