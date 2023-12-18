package com.example.pdfservice.api;

import com.example.pdfservice.dto.EventDTOInput;
import com.example.pdfservice.dto.EventDTOOutput;
import com.example.pdfservice.dto.EventDTOOutputWithTemplate;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.entity.User;
import com.example.pdfservice.enums.Template;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.mapper.EventMapper;
import com.example.pdfservice.repo.EventRepository;
import com.example.pdfservice.repo.UserRepository;
import com.example.pdfservice.service.EventService;
import javax.servlet.http.HttpServletRequest;

import com.example.pdfservice.utils.CalendarUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/event-management")
@AllArgsConstructor
public class EventController {

    private EventService eventService;

    private EventMapper eventMapper;

    private UserRepository userRepository;

    private EventRepository eventRepository;



    @GetMapping("events")
    public ResponseEntity<List<EventDTOOutput>> getAll(HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            List<EventDTOOutput> events=eventService.getAllDto(userEmail);
            return new ResponseEntity<>(events, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("events/id/{id}")
    public ResponseEntity<EventDTOOutput> getById(@PathVariable Long id, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            EventDTOOutputWithTemplate event=eventService.getDtoById(id, userEmail);
            //
            //
           EventDTOOutput ev=eventMapper.toDTO(eventMapper.toEntityFromOut(event));
            return new ResponseEntity<>(ev, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("events/eventName/{eventName}")
    public ResponseEntity<EventDTOOutput> getByEventName(@PathVariable String eventName, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            EventDTOOutputWithTemplate event=eventService.getDtoByEventName(eventName, userEmail);

            EventDTOOutput ev=eventMapper.toDTO(eventMapper.toEntityFromOut(event));
            return new ResponseEntity<>(ev, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("events/date/{date}")
    public ResponseEntity<List<EventDTOOutput>> getByEventDate(@PathVariable String date, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();

            List<EventDTOOutputWithTemplate> events=eventService.getDtoByEventDate(CalendarUtil.getCalendar(date), userEmail);

            List<EventDTOOutput> evs=events.stream().map(event-> {
                try {
                    return eventMapper.toDTO(eventMapper.toEntityFromOut(event));
                } catch (CalendarException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            return new ResponseEntity<>(evs, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("events")
    public ResponseEntity<EventDTOOutput> createEvent(@RequestParam("additionalText") String additionalText, @RequestParam("date") String date,
                                                      @RequestParam("eventName") String eventName, @RequestParam("hours") Integer hours,
                                                      @RequestParam("mainText") String mainText, @RequestParam("template") Template template,
                                                      @RequestParam("title") String title, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();

            EventDTOInput eventDTOInput=new EventDTOInput(eventName,title,hours,mainText,additionalText,date,template);
            Event event=eventService.create(eventDTOInput, userEmail);
            return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

@PostMapping("events/{id}/addUser/{email}")
    public ResponseEntity <EventDTOOutput> addUser(@PathVariable Long id, @PathVariable String email, HttpServletRequest request){
    try {
        String userEmail = request.getUserPrincipal().getName();

        Event event=eventMapper.toEntityFromOut(eventService.getDtoById(id, userEmail));
        User user = userRepository.findUserByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));
        event.addUser(user);
        user.addEvent(event);

        eventRepository.save(event);
        userRepository.save(user);

        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.OK);


    }catch (Exception e){
        return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



}
