package com.example.pdfservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CertificateDTOOutput {

    String fullName;
    String personalInfo;
    String email;
    Boolean hasCode;
    String link;
    Long eventId;
}
