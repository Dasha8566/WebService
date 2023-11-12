package com.example.pdfservice.api;

import com.example.pdfservice.service.PdfService;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("api/v1/pdf-management")
@AllArgsConstructor
public class PdfController {
    private PdfService pdfService;

    @GetMapping("pdf/{certId}")
    public ResponseEntity<Resource>getPdf(@PathVariable Long certId, HttpServletRequest request){
        try{
            String userEmail = request.getUserPrincipal().getName();
            ByteArrayResource file = new ByteArrayResource(pdfService.getPdfByCertId(certId, userEmail));
            return ResponseEntity.ok().header(CONTENT_DISPOSITION, "attachment; filename=\"" + "file.pdf" + "\"").body(file);

        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
