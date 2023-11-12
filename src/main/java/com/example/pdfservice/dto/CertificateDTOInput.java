package com.example.pdfservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CertificateDTOInput {

    String fullName;
    String personalInfo;
    String email;
    Boolean hasCode;
    Long eventId;
}
