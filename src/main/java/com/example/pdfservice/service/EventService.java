package com.example.pdfservice.service;

import com.example.pdfservice.dto.EventDTOInput;
import com.example.pdfservice.dto.EventDTOOutput;
import com.example.pdfservice.dto.EventDTOOutputWithTemplate;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.entity.User;
import com.example.pdfservice.enums.UserPermission;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.exceptions.DataBaseException;
import com.example.pdfservice.mapper.EventMapper;
import com.example.pdfservice.repo.EventRepository;
import com.example.pdfservice.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private EventRepository eventRepository;
    private EventMapper eventMapper;

    private UserRepository userRepository;

    public List<EventDTOOutput> getAllDto(String userEmail){
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));

        if(user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)) {
            return eventRepository.findAll()
                    .stream()
                    .map(item -> eventMapper.toDTO(item))
                    .collect(Collectors.toList());
        }else{
            return user.getEvents()
                    .stream()
                    .map(item -> eventMapper.toDTO(item))
                    .collect(Collectors.toList());
        }
    }

    public EventDTOOutputWithTemplate getDtoById(@NonNull Long id, String userEmail) throws DataBaseException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));
        Event event = eventRepository.findById(id).orElseThrow(()->new DataBaseException("Event with id="+id+" doesn't exist"));
        if(user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                || event.getUsers().contains(user)){
            return eventMapper.toDTOWithTemplate(event);
        }else {
            throw new AccessException("You don't have an access to this event, id="+id);
        }
    }

    public EventDTOOutputWithTemplate getDtoByEventName(@NonNull String eventName, String userEmail) throws DataBaseException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));
        Event event = eventRepository.findEventByEventName(eventName).orElseThrow(()->new DataBaseException("Event with eventName="+eventName+" doesn't exist"));
        if(user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                || event.getUsers().contains(user)){
            return eventMapper.toDTOWithTemplate(event);
        }else {
            throw new AccessException("You don't have an access to this event, eventName="+eventName);
        }
    }

    public List<EventDTOOutputWithTemplate> getDtoByEventDate(@NonNull Calendar date, String userEmail) throws DataBaseException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with email=" + userEmail));
        List<Event> events = eventRepository.findAllByDate(date);
        if (!events.isEmpty()) {
            List<EventDTOOutputWithTemplate> eventsRes = new ArrayList<>();
            int accessExc = 0;
            for (Event e : events) {
                if (user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                        || e.getUsers().contains(user)) {
                    eventsRes.add(eventMapper.toDTOWithTemplate(e));
                } else {
                    accessExc++;
                }
            }
            if (!eventsRes.isEmpty() || accessExc == 0) {
                return eventsRes;
            } else {
                throw new AccessException("You don't have an access to this event, date=" + date);
            }
        }
    else {
         throw new DataBaseException("Event with date=" + date + " doesn't exist");
        }

    }
    public Event create(@NonNull EventDTOInput eventDTOInput, @NonNull String userEmail) throws CalendarException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));
        Event event=eventMapper.toEntity(eventDTOInput);
        event.addUser(user);
        event = eventRepository.save(event);
        user.addEvent(event);
        userRepository.save(user);
        return event;
    }

}

