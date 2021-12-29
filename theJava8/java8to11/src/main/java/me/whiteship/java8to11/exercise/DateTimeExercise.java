package me.whiteship.java8to11.exercise;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeExercise {

    public static void main(String[] args) throws InterruptedException {
        /*
        // 기존 Date 관련 타입
        Date date = new Date();
        Calendar calendar = new GregorianCalendar(1992, Calendar.AUGUST, 27);
        SimpleDateFormat dateFormat = new SimpleDateFormat();

        long time = date.getTime();
        System.out.println(date);
        System.out.println(time);

        // mutable 하기 때문에 multi-thread 환경에서 안전하지 않음
        Thread.sleep(1000 * 3);
        Date after3Seconds = new Date();
        System.out.println(after3Seconds);
        after3Seconds.setTime(time);
        System.out.println(after3Seconds);
        */

        Instant instant = Instant.now();
        System.out.println(instant);    // 기준시 UTC GMT

        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        System.out.println(zonedDateTime);

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        LocalDateTime birthDay = LocalDateTime.of(1992, Month.AUGUST, 27, 0, 0, 0);
        System.out.println(birthDay);

        ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        System.out.println(nowInKorea);

        Instant nowInstant = Instant.now();
        ZonedDateTime zonedDateTime1 = nowInstant.atZone(ZoneId.of("Asia/Seoul"));
        System.out.println(zonedDateTime1);

        LocalDate today = LocalDate.now();
        LocalDate thisYearBirthDay = LocalDate.of(2022, Month.AUGUST, 27);

        Period period = Period.between(today, thisYearBirthDay);
        System.out.println(period);

        Period until = today.until(thisYearBirthDay);
        System.out.println(period.get(ChronoUnit.DAYS));

        long days = ChronoUnit.DAYS.between(today, thisYearBirthDay);
        System.out.println(days);

        Instant plus = nowInstant.plus(10, ChronoUnit.SECONDS);
        Duration between = Duration.between(nowInstant, plus);
        System.out.println(between.getSeconds());

        System.out.println(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        DateTimeFormatter MMddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        System.out.println(now.format(MMddyyyy));

        LocalDate parse = LocalDate.parse("08/27/1992", MMddyyyy);
        System.out.println(parse);
    }

}
