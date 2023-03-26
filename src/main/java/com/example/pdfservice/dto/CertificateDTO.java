package com.example.pdfservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CertificateDTO {

    Long id;
    String fullName;
    String personalInfo;
    String date;
    String email;
    Boolean hasLink;
    String link;
    Long eventId;
}
