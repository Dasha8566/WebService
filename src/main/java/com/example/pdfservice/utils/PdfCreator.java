package com.example.pdfservice.utils;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.Document;
import lombok.NonNull;
import org.springframework.stereotype.Component;


import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfCreator {

    public byte [] generatePDF(@NonNull String templateHtml){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer;
        WriterProperties writerProperties = new WriterProperties();

        writer = new PdfWriter(outputStream, writerProperties);

        PdfDocument pdfDoc = new PdfDocument(writer);

        Document document;
        document = new Document(pdfDoc);

        List<IElement> bodyElements = HtmlConverter.convertToElements(templateHtml);
        for (IElement element : bodyElements) {
            document.add((IBlockElement) element);
        }

        document.close();

        return outputStream.toByteArray();

    }

}
