package com.example.pdfservice.utils;


import com.example.pdfservice.exceptions.CalendarException;
import lombok.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

    private static final SimpleDateFormat DATE_FORMATTER=new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static Calendar getCalendar(@NonNull String dateString) throws CalendarException {
        if (dateString.length()==10) {
            dateString += " 00:00:00.000";
        }
        Calendar calendar = getCalendar();
        try {
            calendar.setTime(TIMESTAMP_FORMATTER.parse(dateString));
        } catch (ParseException ex) {
            throw new CalendarException("The date is not in correct format "+DATE_FORMATTER.toPattern());
        }
        return calendar;
    }

    public static String getDate(@NonNull Long timestamp) {

        return DATE_FORMATTER.format(new Date(timestamp));
    }

    public static String getDate(@NonNull Calendar calendar) {
        return getDate(calendar.getTimeInMillis());
    }
}
