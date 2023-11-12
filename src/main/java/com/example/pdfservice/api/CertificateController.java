package com.example.pdfservice.api;

import com.example.pdfservice.dto.CertificateDTOInput;
import com.example.pdfservice.dto.CertificateDTOOutput;
import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.mapper.CertificateMapper;
import com.example.pdfservice.repo.UserRepository;
import com.example.pdfservice.service.CertificateService;
import javax.servlet.http.HttpServletRequest;

import com.example.pdfservice.utils.CalendarUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.security.SecureRandom;

@RestController
@RequestMapping("api/v1/certificate-management")
@AllArgsConstructor
public class CertificateController {
    private CertificateService certificateService;
    private CertificateMapper certificateMapper;

    private UserRepository userRepository;


    @GetMapping("certificates/code/{code}")
    ResponseEntity<CertificateDTOOutput> getCertByCode(@PathVariable String code, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            CertificateDTOOutput cert=certificateService.getDtoByCode(code, userEmail);
            return new ResponseEntity<>(cert, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("certificates/eventId/{eventId}")
    ResponseEntity<List<CertificateDTOOutput>> getAllByEventId(@PathVariable Long eventId, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            List<CertificateDTOOutput> certs=certificateService.getDtoByEventId(eventId, userEmail);
            return new ResponseEntity<>(certs, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("certificates/eventName/{eventName}")
    ResponseEntity<List<CertificateDTOOutput>> getAllByEventName(@PathVariable String eventName, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            List<CertificateDTOOutput> certs=certificateService.getDtoByEventName(eventName, userEmail);
            return new ResponseEntity<>(certs, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("certificates/date/{date}")
    ResponseEntity<List<CertificateDTOOutput>> getAllByEventDate(@PathVariable String date, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            List<CertificateDTOOutput> certs=certificateService.getDtoByEventDate(CalendarUtil.getCalendar(date), userEmail);
            return new ResponseEntity<>(certs, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("certificates/fullName/{fullName}")
    ResponseEntity<List<CertificateDTOOutput>> getAllByCertificateFullName(@PathVariable String fullName, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            List<CertificateDTOOutput> certs=certificateService.getDtoByCertificateFullName(fullName, userEmail);
            return new ResponseEntity<>(certs, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("certificates/email/{email}")
    ResponseEntity<List<CertificateDTOOutput>> getAllByCertificateEmail(@PathVariable String email, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            List<CertificateDTOOutput> certs=certificateService.getDtoByCertificateEmail(email, userEmail);
            return new ResponseEntity<>(certs, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("certificates")
    ResponseEntity<CertificateDTOOutput> createCertificate(@RequestParam("email") String email,
                                                           @RequestParam("eventId") Long eventId, @RequestParam("fullName") String fullName,
                                                           @RequestParam("hasCode") Boolean hasCode,
                                                           @RequestParam("personalInfo") String personalInfo,
                                                           HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();


            CertificateDTOInput certificateDTOInput=new CertificateDTOInput(fullName,personalInfo,email,hasCode,eventId);
            Certificate certificate = certificateService.save(certificateDTOInput, userEmail);
            return new ResponseEntity<>(certificateMapper.toDTO(certificate), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("certificates/csv")
    ResponseEntity<List<CertificateDTOOutput>> importFromCSV(@RequestPart("file") MultipartFile file, HttpServletRequest request){
        try(BufferedReader br= new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String userEmail = request.getUserPrincipal().getName();
List<CertificateDTOOutput> certsRes=new ArrayList<>();

            String str="";
            while((str=br.readLine())!=null){
                StringTokenizer tokenizer=new StringTokenizer(str,",");


                String fullName=(String)tokenizer.nextElement();
                String personalInfo=(String)tokenizer.nextElement();
                String email=(String)tokenizer.nextElement();
                String bool=(String)tokenizer.nextElement();
                Boolean hasCode=false;
                if ("true".equalsIgnoreCase(bool)) {
                    hasCode = Boolean.TRUE;
                } else if ("false".equalsIgnoreCase(bool)) {
                    hasCode = Boolean.FALSE;
                }

                Long eventId=(Long.parseLong((String)(tokenizer.nextElement())));



                CertificateDTOInput certificateDTOInput=new CertificateDTOInput(fullName,personalInfo,email,hasCode,eventId);
                    Certificate certificate = certificateService.save(certificateDTOInput, userEmail);
                    certsRes.add(certificateMapper.toDTO(certificate));

            }

            return new ResponseEntity<>(certsRes, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
