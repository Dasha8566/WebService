package com.example.pdfservice.utils;

import com.example.pdfservice.entity.Certificate;
import com.example.pdfservice.enums.Template;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

@Component
@AllArgsConstructor


public class PdfGenerator {
    private PdfCreator pdfCreator;
    private ResourceLoader resourceLoader;


    public byte[] generatePdf(@NonNull Certificate certificate) throws IOException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        String htmlTemplate = loadHTMLTemplate(certificate.getEvent().getTemplate(), resourceLoader);
        htmlTemplate=htmlTemplate.replace("#full-name#",certificate.getFullName());
        htmlTemplate=htmlTemplate.replace("#personal-info#", certificate.getPersonalInfo());
        htmlTemplate=htmlTemplate.replace("#email#", certificate.getEmail());
        htmlTemplate=htmlTemplate.replace("#date#", "Дата заходу: "+CalendarUtil.getDate(certificate.getEvent().getDate()));
        htmlTemplate=htmlTemplate.replace("#event-name#", "Назва заходу: "+certificate.getEvent().getEventName());
        htmlTemplate=htmlTemplate.replace("#title#", certificate.getEvent().getTitle());
        htmlTemplate=htmlTemplate.replace("#main-text#",certificate.getEvent().getMainText());

        htmlTemplate=htmlTemplate.replace("#add-text#","Додаткова інформація: "+certificate.getEvent().getAdditionalText());
        if(htmlTemplate.contains("#hours#")){
            if(certificate.getEvent().getHours()!=null && certificate.getEvent().getHours()>0){
                htmlTemplate=htmlTemplate.replace("#hours#","Кількість годин: "+certificate.getEvent().getHours());
            }else{
                htmlTemplate=htmlTemplate.replace("#hours#","");
            }


        }
        if(certificate.getHasLink()==true){

                htmlTemplate=htmlTemplate.replace("#link#","Посилання: "+certificate.getLink());
            }else{
                htmlTemplate=htmlTemplate.replace("#link#","");
            }

        htmlTemplate=htmlTemplate.replace("#date2#", "Отримано: "+CalendarUtil.getDate(certificate.getDate2()));

        //System.out.println(htmlTemplate);
        return pdfCreator.generatePDF(htmlTemplate);
    }


    private String loadHTMLTemplate(@NonNull Template template, @NonNull ResourceLoader resourceLoader) throws IOException {
        /*Charset charset = Charset.forName("UTF-8");*/
        InputStream resourceInputStream = resourceLoader.getResource("classpath:templates/"+template.getFileName()).getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceInputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

}
