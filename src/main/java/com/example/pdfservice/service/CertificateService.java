package com.example.pdfservice.service;

import com.example.pdfservice.dto.CertificateDTOInput;
import com.example.pdfservice.dto.CertificateDTOOutput;
import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.entity.Event;
import com.example.pdfservice.entity.User;
import com.example.pdfservice.enums.UserPermission;
import com.example.pdfservice.exceptions.CalendarException;
import com.example.pdfservice.exceptions.DataBaseException;
import com.example.pdfservice.mapper.CertificateMapper;
import com.example.pdfservice.repo.CertificateRepository;
import com.example.pdfservice.repo.EventRepository;
import com.example.pdfservice.repo.UserRepository;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;

@Service
@AllArgsConstructor
public class CertificateService {
    private CertificateRepository certificateRepository;
    private EventRepository eventRepository;

    private CertificateMapper certificateMapper;

    private UserRepository userRepository;

    //private Environment environment;
    public List<Certificate>getByEvent(Event event){
        return certificateRepository.findAllByEvent(event);
    }

    private static final String ALLOWED_CHARACTERS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final int CODE_LENGTH = 6;


    public CertificateDTOOutput getDtoByCode(@NonNull String code, String userEmail) throws DataBaseException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));


            if(user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)) {
                return certificateMapper.toDTO(certificateRepository.findByCode(code));

            }
            return null;

    }




    public List<CertificateDTOOutput>getDtoByEventId(@NonNull Long eventId, String userEmail) throws DataBaseException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if(eventOpt.isPresent()) {
        if(user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)) {
            return certificateRepository.findAllByEvent(eventOpt.get())
                    .stream()
                    .map(item->certificateMapper.toDTO(item))
                    .collect(Collectors.toList());
        }else{
            if(user.getEvents().contains(eventOpt.get())){
                return certificateRepository.findAllByEvent(eventOpt.get())
                        .stream()
                        .map(item->certificateMapper.toDTO(item))
                        .collect(Collectors.toList());
            }else {
                throw new AccessException("You don't have an access to these certificates with eventId=" + eventId);
            }
        }



        }else{
            throw new DataBaseException("Event with id="+eventId+" doesn't exist");
        }
    }

    public List<CertificateDTOOutput>getDtoByEventName(@NonNull String eventName, String userEmail) throws DataBaseException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));

        Optional<Event> eventOpt = eventRepository.findEventByEventName(eventName);
        if(eventOpt.isPresent()) {
            if(user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)) {
                return certificateRepository.findAllByEvent(eventOpt.get())
                        .stream()
                        .map(item->certificateMapper.toDTO(item))
                        .collect(Collectors.toList());
            }else{
                if(user.getEvents().contains(eventOpt.get())){
                    return certificateRepository.findAllByEvent(eventOpt.get())
                            .stream()
                            .map(item->certificateMapper.toDTO(item))
                            .collect(Collectors.toList());
                }else {
                    throw new AccessException("You don't have an access to these certificates with eventName=" + eventName);
                }
            }



        }else{
            throw new DataBaseException("Event with eventName="+eventName+" doesn't exist");
        }
    }


    public List<CertificateDTOOutput>getDtoByEventDate(@NonNull Calendar date, String userEmail) throws DataBaseException, AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));

        List<Event> events = eventRepository.findAllByDate(date);
        if(!events.isEmpty()) {

            if (user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)) {
                List<CertificateDTOOutput> certs=new ArrayList<>();
                for(Event e:events) {

                           certs.addAll(certificateRepository.findAllByEvent(e)
                            .stream()
                            .map(item -> certificateMapper.toDTO(item))
                            .collect(Collectors.toList()));

                }
                return certs;
            } else {
                List<CertificateDTOOutput> certs=new ArrayList<>();
                int accessExc=0;
                for (Event e : events) {
                    if (user.getEvents().contains(e)) {
                        certs.addAll(certificateRepository.findAllByEvent(e)
                                .stream()
                                .map(item -> certificateMapper.toDTO(item))
                                .collect(Collectors.toList()));
                    }else{
                        accessExc++;
                }
                }
                if(!certs.isEmpty()||accessExc==0) {
                    return certs;
                }else {
                    throw new AccessException("You don't have an access to these certificates with date=" + date);
                }
                }
            }
        else{
            throw new DataBaseException("Event with date="+date+" doesn't exist");
        }
    }

    public List<CertificateDTOOutput>getDtoByCertificateFullName(@NonNull String fullName, String userEmail) throws AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with email=" + userEmail));

        List<Certificate> certs = certificateRepository.findAllByFullName(fullName);
        if (!certs.isEmpty()) {
            List<CertificateDTOOutput> certsRes=new ArrayList<>();
            int accessExc=0;
            for(Certificate c:certs){
            if (user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                    || c.getEvent().getUsers().contains(user)) {
                 certsRes.add(certificateMapper.toDTO(c));
            } else {
                accessExc++;
            }
        }
            if(!certsRes.isEmpty()||accessExc==0) {
                return certsRes;
            }else {
                throw new AccessException("You don't have an access to these certificates with fullName=" + fullName);
            }
    }else{
            return Collections.EMPTY_LIST;
        }

    }


    public List<CertificateDTOOutput>getDtoByCertificateEmail(@NonNull String email, String userEmail) throws AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with email=" + userEmail));

        List<Certificate> certs = certificateRepository.findAllByEmail(email);
        if (!certs.isEmpty()) {
            List<CertificateDTOOutput> certsRes=new ArrayList<>();
            int accessExc=0;
            for(Certificate c:certs){
                if (user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                        || c.getEvent().getUsers().contains(user)) {
                    certsRes.add(certificateMapper.toDTO(c));
                } else {
                    accessExc++;
                }
            }
            if(!certsRes.isEmpty()||accessExc==0) {
                return certsRes;
            }else {
                throw new AccessException("You don't have an access to these certificates with email=" + email);
            }
        }else{
            return Collections.EMPTY_LIST;
        }

    }
    public Optional<Certificate> getById(@NonNull Long id, String userEmail) throws AccessException {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));

        Optional<Certificate> certOpt=certificateRepository.findById(id);
        if(certOpt.isPresent()) {
            if (user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                    || certOpt.get().getEvent().getUsers().contains(user)) {
                return certificateRepository.findById(id);
            }else{
                throw new AccessException("You don't have an access to this certificate with id="+id);
            }
        }else{
            return Optional.empty();
        }

    }

    public Certificate save(@NonNull CertificateDTOInput certificateDTOInput, String userEmail) throws CalendarException, DataBaseException, AccessException, NoSuchAlgorithmException {

        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->new UsernameNotFoundException("User not found with email="+userEmail));

        Optional<Event> eventOpt = eventRepository.findById(certificateDTOInput.getEventId());
        if(eventOpt.isPresent()) {
            if(user.getRole().getPermissions().contains(UserPermission.ADMIN_PERMISSION)
                || eventOpt.get().getUsers().contains(user)){


                Certificate certificate=certificateRepository.save(certificateMapper.toEntity(certificateDTOInput,eventOpt.get()));
                if(certificateDTOInput.getHasCode()){

                    String uniqueCode;
                    do {
                        uniqueCode = generateSerialNumber();
                    } while (certificateRepository.existsByCode(uniqueCode));


                    certificate.setCode(uniqueCode);
                    certificateRepository.save(certificate);
                }
                return certificate;
        }else{
                throw new AccessException("You don't have an access to this certificate with eventId="+certificateDTOInput.getEventId());
            }




        }else{
            throw new DataBaseException("Event with id="+certificateDTOInput.getEventId()+" doesn't exist");
        }

    }

    private String generateSerialNumber() throws NoSuchAlgorithmException {

        final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        //String domain = environment.getProperty("code.domain");

        StringBuilder code = new StringBuilder();
        //code.append(domain);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            code.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }


}
