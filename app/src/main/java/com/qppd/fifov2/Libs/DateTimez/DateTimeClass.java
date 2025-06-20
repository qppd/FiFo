package com.qppd.fifov2.Libs.DateTimez;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeClass {

    SimpleDateFormat sdf;
    Calendar calendar;

    public DateTimeClass(){

    }

    public DateTimeClass(String format){

        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat(format, Locale.US);

    }

    public String getFormattedTime(){
        return sdf.format(calendar.getTime());
    }

    public Date getDateFromString(String value){
        try {

            return sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getMonth(){
        return calendar.get(Calendar.DATE);
        //return calendar.get(Calendar.MONTH);;
    }

    public int getEndDate(){
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public int getCurrentDay(){
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getYear(){
        return calendar.get(Calendar.YEAR);
    }


    public int calculateAge(int birthYear, int birthMonth, int birthDay) {
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--;
        }
        return age;
    }

}
