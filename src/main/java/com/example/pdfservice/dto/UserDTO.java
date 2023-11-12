package com.example.pdfservice.dto;

import com.example.pdfservice.enums.UserRole;
import com.example.pdfservice.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDTO {
    Long id;
    String email;
    UserStatus status;
    UserRole role;
}
