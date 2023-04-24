package com.example.pdfservice.utils;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import lombok.NonNull;
import org.springframework.stereotype.Component;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.util.Properties;


@Component
public class PdfCreator {



    public byte[] generatePDF(@NonNull String templateHtml) throws IOException {


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();









        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(templateHtml, outputStream,
                converterProperties);




        return outputStream.toByteArray();

    }
   }








