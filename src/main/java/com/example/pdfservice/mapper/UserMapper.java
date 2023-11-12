package com.example.pdfservice.mapper;


import com.example.pdfservice.dto.UserDTO;
import com.example.pdfservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user){
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getStatus(),
                user.getRole()
        );
    }
}