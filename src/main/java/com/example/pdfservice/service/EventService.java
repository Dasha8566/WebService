package com.example.pdfservice.service;

import com.example.pdfservice.dto.EventDTO;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.exceptions.DataBaseException;
import com.example.pdfservice.mapper.EventMapper;
import com.example.pdfservice.repo.EventRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private EventRepository eventRepository;
    private EventMapper eventMapper;

    public List<EventDTO> getAllDto(){
        return eventRepository.findAll()
                .stream()
                .map(item->eventMapper.toDTO(item))
                .collect(Collectors.toList());
    }

    public EventDTO getDtoById(@NonNull Long id) throws DataBaseException {
        Optional <Event> eventOpt =eventRepository.findById(id);
        if(eventOpt.isPresent()) {
            return eventMapper.toDTO(eventOpt.get());
        }else {
            throw new DataBaseException("Event with id="+id+" doesn't exist");
        }
    }

    public Event save(@NonNull EventDTO eventDTO) throws CalendarException {
        Event event=eventMapper.toEntity(eventDTO);
        return eventRepository.save(event);
    }

}
