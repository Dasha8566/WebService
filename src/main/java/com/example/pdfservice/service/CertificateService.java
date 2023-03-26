package com.example.pdfservice.service;

import com.example.pdfservice.dto.CertificateDTO;
import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.exceptions.DataBaseException;
import com.example.pdfservice.mapper.CertificateMapper;
import com.example.pdfservice.repo.CertificateRepository;
import com.example.pdfservice.repo.EventRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CertificateService {
    private CertificateRepository certificateRepository;
    private EventRepository eventRepository;

    private CertificateMapper certificateMapper;

    public List<Certificate>getByEvent(Event event){
        return certificateRepository.findAllByEvent(event);
    }

    public List<CertificateDTO>getDtoByEventId(@NonNull Long eventId) throws DataBaseException {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if(eventOpt.isPresent()) {
            return certificateRepository.findAllByEvent(eventOpt.get())
                    .stream()
                    .map(item->certificateMapper.toDTO(item))
                    .collect(Collectors.toList());
        }else{
            throw new DataBaseException("Event with id="+eventId+" doesn't exist");
        }
    }

    public Optional<Certificate> getById(@NonNull Long id){
        return certificateRepository.findById(id);
    }

    public Certificate save(@NonNull CertificateDTO certificateDTO) throws CalendarException, DataBaseException {
        Optional<Event> eventOpt = eventRepository.findById(certificateDTO.getEventId());
        if(eventOpt.isPresent()) {
            return certificateRepository.save(certificateMapper.toEntity(certificateDTO,eventOpt.get()));
        }else{
            throw new DataBaseException("Event with id="+certificateDTO.getEventId()+" doesn't exist");
        }

    }



}
