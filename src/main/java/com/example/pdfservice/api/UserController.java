package com.example.pdfservice.api;

import com.example.pdfservice.dto.UserDTO;
import com.example.pdfservice.enums.UserRole;
import com.example.pdfservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user-management")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("users")
    @PreAuthorize("hasAuthority('admin_permission')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("users/{id}")
    @PreAuthorize("hasAuthority('admin_permission')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("user/{email}")
    @PreAuthorize("hasAuthority('admin_permission')")
    public ResponseEntity<UserDTO> createUser(@PathVariable String email){
        try {
            return new ResponseEntity<>(userService.createUser(email), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("change/role/{id}/{role}")
    @PreAuthorize("hasAuthority('super_admin_permission')")
    public ResponseEntity<UserDTO> changeRole(@PathVariable Long id, @PathVariable UserRole userRole){
        try {
            return new ResponseEntity<>(userService.setRole(id,userRole), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
