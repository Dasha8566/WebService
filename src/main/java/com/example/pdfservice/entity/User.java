package com.example.pdfservice.entity;

import com.example.pdfservice.enums.UserRole;
import com.example.pdfservice.enums.UserStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = PRIVATE)
public class User implements UserDetails {

    static final long serialVersionUID = 1L;

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(updatable = false)
    Long id;

    @Version
    @Column(updatable = true)
    Long version;

    @Getter
    @Column(name="email",unique = true,updatable = false)
    String email;

    @Column(name = "password")
    String password;

    @Getter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    UserStatus status;

    @Getter
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    UserRole role;

    @ManyToMany
    @JoinTable(
            name = "user_to_event",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    List<Event> events;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.status = UserStatus.ACTIVE;
        this.role = UserRole.USER;
        this.events = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status.equals(UserStatus.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(UserStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status.equals(UserStatus.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return status.equals(UserStatus.ACTIVE);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void addEvent(Event event){
        event.addUser(this);
        events.add(event);
    }
}