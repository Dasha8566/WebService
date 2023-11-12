package com.example.pdfservice.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class EventDTOOutput {

    String eventName;
    String title;
    Integer hours;
    String mainText;
    String additionalText;
    String date;
}
