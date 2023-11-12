package com.example.pdfservice.mapper;

import com.example.pdfservice.dto.EventDTOInput;
import com.example.pdfservice.dto.EventDTOOutput;
import com.example.pdfservice.dto.EventDTOOutputWithTemplate;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.utils.CalendarUtil;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDTOOutput toDTO(Event event){
        return new EventDTOOutput(
                event.getEventName(),
                event.getTitle(),
                event.getHours(),
                event.getMainText(),
                event.getAdditionalText(),
                CalendarUtil.getDate(event.getDate())
        );
    }


    public EventDTOOutputWithTemplate toDTOWithTemplate(Event event){
        return new EventDTOOutputWithTemplate(
                event.getEventName(),
                event.getTitle(),
                event.getHours(),
                event.getMainText(),
                event.getAdditionalText(),
                CalendarUtil.getDate(event.getDate()),
                event.getTemplate()
                );
    }

    public Event toEntity(EventDTOInput eventDTOInput) throws CalendarException {
        return new Event(
                eventDTOInput.getEventName(),
                eventDTOInput.getTitle(),
                eventDTOInput.getHours(),
                eventDTOInput.getMainText(),
                eventDTOInput.getAdditionalText(),
                CalendarUtil.getCalendar(eventDTOInput.getDate()),
                eventDTOInput.getTemplate()
        );
    }

    public Event toEntityFromOut(EventDTOOutputWithTemplate eventDTOOutputWithTemplate) throws CalendarException {
        return new Event(
                eventDTOOutputWithTemplate.getEventName(),
                eventDTOOutputWithTemplate.getTitle(),
                eventDTOOutputWithTemplate.getHours(),
                eventDTOOutputWithTemplate.getMainText(),
                eventDTOOutputWithTemplate.getAdditionalText(),
                CalendarUtil.getCalendar(eventDTOOutputWithTemplate.getDate()),
                eventDTOOutputWithTemplate.getTemplate()
        );
    }
}
