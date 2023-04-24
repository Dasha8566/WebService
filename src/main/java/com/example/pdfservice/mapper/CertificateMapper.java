package com.example.pdfservice.mapper;

import com.example.pdfservice.dto.CertificateDTO;
import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.utils.CalendarUtil;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    public CertificateDTO toDTO(Certificate certificate){
        return new CertificateDTO(
                certificate.getId(),
                certificate.getFullName(),
                certificate.getPersonalInfo(),
                CalendarUtil.getDate(certificate.getDate2()),
                certificate.getEmail(),
                certificate.getHasLink(),
                certificate.getLink(),
                certificate.getEvent().getId()
        );
    }

    public Certificate toEntity(CertificateDTO certificateDTO, Event event) throws CalendarException {
        return new Certificate(
                certificateDTO.getFullName(),
                certificateDTO.getPersonalInfo(),
                CalendarUtil.getCalendar(certificateDTO.getDate2()),
                certificateDTO.getEmail(),
                certificateDTO.getHasLink(),
                certificateDTO.getLink(),
                event
        );
    }
}
