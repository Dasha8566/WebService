package com.example.pdfservice.dto;


import com.example.pdfservice.enums.Template;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EventDTOInput {

    String eventName;
    String title;
    Integer hours;
    String mainText;
    String additionalText;
    String date;
    Template template;
}
