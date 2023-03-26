package com.example.pdfservice.mapper;

import com.example.pdfservice.dto.EventDTO;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.utils.CalendarUtil;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDTO toDTO(Event event){
        return new EventDTO(
                event.getId(),
                event.getEventName(),
                event.getTitle(),
                event.getHours(),
                event.getMainText(),
                event.getAdditionalText(),
                CalendarUtil.getDate(event.getDate()),
                event.getTemplate()
        );
    }

    public Event toEntity(EventDTO eventDTO) throws CalendarException {
        return new Event(
                eventDTO.getEventName(),
                eventDTO.getTitle(),
                eventDTO.getHours(),
                eventDTO.getMainText(),
                eventDTO.getAdditionalText(),
                CalendarUtil.getCalendar(eventDTO.getDate()),
                eventDTO.getTemplate()
        );
    }
}
