package com.example.pdfservice.mapper;

import com.example.pdfservice.dto.CertificateDTOInput;
import com.example.pdfservice.dto.CertificateDTOOutput;
import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.utils.CalendarUtil;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    public CertificateDTOOutput toDTO(Certificate certificate){
        return new CertificateDTOOutput(
                certificate.getFullName(),
                certificate.getPersonalInfo(),
                certificate.getEmail(),
                certificate.getHasCode(),
                certificate.getCode(),
                certificate.getEvent().getId()
        );
    }

    public Certificate toEntity(CertificateDTOInput certificateDTOInput, Event event) throws CalendarException {
        return new Certificate(
                certificateDTOInput.getFullName(),
                certificateDTOInput.getPersonalInfo(),
                certificateDTOInput.getEmail(),
                certificateDTOInput.getHasCode(),
                event
        );
    }
}
