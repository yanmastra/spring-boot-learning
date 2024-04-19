package com.yanmastra.msSecurityBase.utils;

import com.yanmastra.msSecurityBase.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTimeUtils {
    private static final DateTimeFormatter dtfShortDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final DateTimeFormatter dtfDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static DateFormat utcDate = null;

    static void initUtcDate(){
        if (utcDate == null) {
            utcDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            utcDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
    }

    public static LocalDateTime toDateTime(String s) {
        try {
            if (s.length() == 20) {
                return LocalDateTime.parse(s, dtfShortDateTime);
            } else {
                return LocalDateTime.parse(s, dtfDateTime);
            }
        } catch (Exception e) {
            Log.log.warn(e.getMessage(), e);
            return null;
        }
    }
}
