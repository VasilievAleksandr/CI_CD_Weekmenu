package by.weekmenu.api.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;

import static java.time.DayOfWeek.*;

public class WeekMenuDatesUtils {

    public static String getWeekDates(int weekNumber) {
        int year = LocalDate.now().getYear();
        int startDate = 1;
        int startMonth = 1;
        LocalDate localDate = LocalDate.of(year, startDate, startMonth);
        if (localDate.getDayOfWeek() == MONDAY) {
            return getArrayOfWeeks(year, startMonth, startDate).get(weekNumber);
        } else {
            if (localDate.getDayOfWeek() == TUESDAY ||
                    localDate.getDayOfWeek() == WEDNESDAY || localDate.getDayOfWeek() == THURSDAY) {
                startDate = 31;
                startMonth = 12;
                while (localDate.getDayOfWeek() != MONDAY) {
                    localDate = LocalDate.of(year - 1, startMonth, startDate);
                    startDate--;
                }
                return getArrayOfWeeks(localDate.getYear(), localDate.getMonth().getValue(), localDate.getDayOfMonth()).get(weekNumber);
            } else {
                while (localDate.getDayOfWeek() != MONDAY) {
                    startDate++;
                    localDate = LocalDate.of(year, startMonth, startDate);
                }
                return getArrayOfWeeks(year - 1, startMonth, startDate).get(weekNumber);
            }
        }
    }

    private static ArrayList<String> getArrayOfWeeks(int year, int startMonth, int startDate) {
        int currentWeek = 1;
        ArrayList<String> arrayOfWeeks = new ArrayList<>();
        arrayOfWeeks.add(null);
        LocalDate localDate = LocalDate.of(year, startMonth, startDate);
        while (currentWeek <= getNumberOfWeeksInTheYear()) {
            arrayOfWeeks.add((String.format("%02d", localDate.getDayOfMonth()) + "."
                    + String.format("%02d", localDate.getMonth().getValue()))
                    .concat(" - " +
                            (String.format("%02d", localDate.plusWeeks(1).minusDays(1).getDayOfMonth())
                                    + "." + String.format("%02d", localDate.plusWeeks(1).minusDays(1).getMonth().getValue()))));
            localDate = localDate.plusWeeks(1);
            currentWeek++;
        }
        arrayOfWeeks.add("XX - XX");
        return arrayOfWeeks;
    }

    public static long getNumberOfWeeksInTheYear() {
        ZoneId zoneId = ZoneId.of("Europe/Minsk");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        return IsoFields.WEEK_OF_WEEK_BASED_YEAR.rangeRefinedBy(now).getMaximum();
    }

    public static int getCurrentWeekNumber() {
        ZoneId zoneId = ZoneId.of("Europe/Minsk");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        return now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }
}
