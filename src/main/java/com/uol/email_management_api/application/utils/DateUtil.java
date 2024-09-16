package com.uol.email_management_api.application.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

    public static String formatDate(LocalDateTime date) {
        if (date == null) {
            return null;
        }

        ZoneId zoneId = ZoneId.systemDefault();
        Date dateAsDate = Date.from(date.atZone(zoneId).toInstant());

        return simpleDateFormat.format(dateAsDate);
    }
}
