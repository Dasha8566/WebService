package com.example.pdfservice.entity;


import com.example.pdfservice.enums.Template;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.Calendar;
import java.util.List;

import static javax.persistence.TemporalType.DATE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = PRIVATE)
public class Event {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(updatable = false)
    Long id;

    @Version
    @Column(updatable = true)
    Long version;

    @Column(name="event_name")
    String eventName;

    @Column(name="title")
    String title;

    @Column(name="hours")
    Integer hours;

    @Column(name="main_text", length = 1000)
    String mainText;

    @Column(name="add_text", length = 1000)
    String additionalText;

    @Column(name="date")
    @Temporal(DATE)
    Calendar date;

    @Column(name="template")
    @Enumerated(EnumType.STRING)
    Template template;

    @OneToMany(mappedBy = "event")
    List<Certificate> certificateList;

    public Event(String eventName, String title, Integer hours, String mainText, String additionalText, Calendar date) {
        this.eventName = eventName;
        this.title = title;
        this.hours = hours;
        this.mainText = mainText;
        this.additionalText = additionalText;
        this.date = date;
    }

    public Event(String eventName, String title, Integer hours, String mainText, String additionalText, Calendar date, Template template) {
        this.eventName = eventName;
        this.title = title;
        this.hours = hours;
        this.mainText = mainText;
        this.additionalText = additionalText;
        this.date = date;
        this.template = template;
    }
}
