package com.example.pdfservice.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.Calendar;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "certificate")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = PRIVATE)
public class Certificate {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(updatable = false)
    Long id;

    @Version
    @Column(updatable = true)
    Long version;

    @Column(name="full_name",nullable = false)
    String fullName;

    @Column(name="personal_info")
    String personalInfo;

    @Column(name="date")
    @Temporal(TemporalType.DATE)
    Calendar date;

    @Column(name="email",nullable = false)
    String email;

    @Column(name="has_link")
    Boolean hasLink;

    @Column(name="link", unique = true)
    String link;

    @ManyToOne
    @JoinColumn(name="event_id")
    Event event;

    public Certificate(String fullName, String personalInfo, Calendar date, String email, Boolean hasLink, String link, Event event) {
        this.fullName = fullName;
        this.personalInfo = personalInfo;
        this.date = date;
        this.email = email;
        this.hasLink = hasLink;
        this.link = link;
        this.event = event;
    }
}
