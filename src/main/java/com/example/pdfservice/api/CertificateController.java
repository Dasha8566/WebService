package com.example.pdfservice.api;

import com.example.pdfservice.dto.CertificateDTO;
import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.mapper.CertificateMapper;
import com.example.pdfservice.service.CertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/certificate-management")
@AllArgsConstructor
public class CertificateController {
    private CertificateService certificateService;
    private CertificateMapper certificateMapper;

    @GetMapping("certificates/{eventId}")
    ResponseEntity<List<CertificateDTO>> getAllByEventId(@PathVariable Long eventId){
        try{
            List<CertificateDTO> certs=certificateService.getDtoByEventId(eventId);
            return new ResponseEntity<>(certs, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("certificates")
    ResponseEntity<CertificateDTO> createCertificate(@RequestBody CertificateDTO certificateDTO){
        try{
            Certificate certificate = certificateService.save(certificateDTO);
            return new ResponseEntity<>(certificateMapper.toDTO(certificate), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
