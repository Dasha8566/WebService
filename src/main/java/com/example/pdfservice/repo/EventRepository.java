package com.example.pdfservice.repo;

import com.example.pdfservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByEventName(String eventName);
    List<Event> findAllByDate(Calendar date);

}