package com.example.pdfservice.config;

import com.example.pdfservice.entity.User;
import com.example.pdfservice.enums.UserRole;
import com.example.pdfservice.enums.UserStatus;
import com.example.pdfservice.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers( "/h2-console/**");
    }

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/certificate/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("*.html").permitAll()
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/swagger-ui/").and()
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CommandLineRunner loadData(UserRepository userRepository){
        return  args -> {
            if(userRepository.findAll().isEmpty()) {
                //Додавання тестових користувачів.
                User user = new User("user", passwordEncoder().encode("user"));
                userRepository.save(user);

                User adminUser = new User("admin", passwordEncoder().encode("admin"));
                adminUser.setRole(UserRole.ADMIN);
                userRepository.save(adminUser);

                User superAdminUser = new User("sadmin", passwordEncoder().encode("sadmin"));
                superAdminUser.setRole(UserRole.SUPER_ADMIN);
                userRepository.save(superAdminUser);

                User inactiveUser = new User("user2", passwordEncoder().encode("user2"));
                inactiveUser.setStatus(UserStatus.INACTIVE);
                userRepository.save(inactiveUser);
            }
        };
    }


}
