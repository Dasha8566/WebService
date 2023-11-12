package com.example.pdfservice.service;

import com.example.pdfservice.dto.UserDTO;
import com.example.pdfservice.entity.User;
import com.example.pdfservice.enums.UserRole;
import com.example.pdfservice.enums.UserStatus;
import com.example.pdfservice.exceptions.EmailException;
import com.example.pdfservice.mapper.UserMapper;
import com.example.pdfservice.repo.UserRepository;
import liquibase.repackaged.org.apache.commons.text.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Value("${service.name}")
    private String serviceName;

    @Value("${service.address}")
    private String serviceAddress;


    public UserDTO createUser(String email) throws EmailException {
        String password = randomString(10);
        User user = new User(email, passwordEncoder.encode(password));
        user = saveUser(user);

        String text = emailText(password);
        String subject = "Реєстрація на сервісі "+serviceName;
        emailService.sendTestEmail(email,text,subject);

        return userMapper.toDto(user);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream().map(user->userMapper.toDto(user)).toList();
    }

    public UserDTO getUserById(Long id){
        return userMapper.toDto(
                userRepository.findById(id)
                        .orElseThrow(
                                ()->new UsernameNotFoundException("User not found, id="+id)
                        )
        );
    }

    public UserDTO setRole(Long id, UserRole role){
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            new UsernameNotFoundException("User not found, id="+id);
        }
        User user = userOptional.get();
        if(user.getRole().equals(UserRole.SUPER_ADMIN)){
            return userMapper.toDto(user);
        }
        user.setRole(role);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    private String emailText(String password){
        String text = "Ви зареєстровані на сервісі "+serviceName+"\n"
                +"Ваш пароль: " + password +"\n"
                +"Щоб зайти на сервіс перейдіть за посиланням:" +serviceAddress +"/login \n"
                +"З метою безпеки, будь-ласка, змініть цей пароль на новий";
        return text;
    }

    private String randomString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }


}