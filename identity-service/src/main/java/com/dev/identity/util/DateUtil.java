package com.dev.identity.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static List<Date[]> getMonthStartEndDates(int year) {
        List<Date[]> monthDates = new ArrayList<>();

        for (Month month : Month.values()) {
            // Ngày đầu tháng
            LocalDate startOfMonthLocal = LocalDate.of(year, month, 1);
            Date startOfMonth = Date.from(startOfMonthLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Ngày cuối tháng
            LocalDate endOfMonthLocal = startOfMonthLocal.withDayOfMonth(startOfMonthLocal.lengthOfMonth());
            Date endOfMonth = Date.from(endOfMonthLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

            monthDates.add(new Date[]{startOfMonth, endOfMonth});
        }

        return monthDates;

    }
}
