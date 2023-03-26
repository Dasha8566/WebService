package com.example.pdfservice.api;

import com.example.pdfservice.dto.EventDTO;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.mapper.EventMapper;
import com.example.pdfservice.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/event-management")
@AllArgsConstructor
public class EventController {

    private EventService eventService;

    private EventMapper eventMapper;


    @GetMapping("events")
    public ResponseEntity<List<EventDTO>> getAll(){
        try{
            List<EventDTO> events=eventService.getAllDto();
            return new ResponseEntity<>(events, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("events/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable Long id){
        try{
            EventDTO event=eventService.getDtoById(id);
            return new ResponseEntity<>(event, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("events")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO){
        try{
            Event event=eventService.save(eventDTO);
            return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
