package com.qppd.fifov2.Libs.Functionz;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by sajed on 9/4/2018.
 */

public class UserMethods {


    // bitmap combination
    public Bitmap combineBitmaps(Bitmap c, Bitmap s) {
        int width, height;
        Bitmap cs;
        Canvas comboImage;
        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth();
            height = c.getHeight();
        }
        else {
            width = s.getWidth();
            height = c.getHeight();
        }
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        comboImage = new Canvas(cs);
        comboImage.drawBitmap(c,0f,0f,null);
        comboImage.drawBitmap(s,comboImage.getWidth() - s.getWidth() - 10 ,comboImage.getHeight()-s.getHeight() -10,null);
        return cs;
    }

    // SnackBar show Message
    public void showSnackBar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    // CHECK IF WEEKDAY
    public boolean isWeekDay(Calendar cal){
        // check if weekend
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return false;
        }
        // IF NOTHING ELSE, IT'S A WEEKDAY
        return true;
    }
    // date default format
    // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    public Date DATE(String strdate){
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = readFormat.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // random number
    public int RANDOM_NUMBER(int from, int to){
        Random random = new Random();
        return random.nextInt( from - to ) + to;
    }

    // weekend counter
    public int WEEKENDCOUNT(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        // Note that month is 0-based in calendar, bizarrely.
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int count = 0;
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                count++;
                // Or do whatever you need to with the result.
            }
        }
        return count;
    }
    // SIRA PA
    // weekday counter from two dates
    public int WEEKDAYCOUNTFROMTWODATES(int year, int month, int date, int month2, int date2) {
        Calendar calendar = Calendar.getInstance();
        // Note that month is 0-based in calendar, bizarrely.
        calendar.set(year, month - 1, date);
        //int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 15;
        int count = 0;

        for(int m = month; m <= month2; m++){
            calendar.set(year,month - 1, date);
            for(int d = date; d <= LASTDAY(new Date()); d++){

                calendar.set(year,month2 - 1, date2);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.TUESDAY ||
                        dayOfWeek == Calendar.WEDNESDAY || dayOfWeek == Calendar.THURSDAY ||
                        dayOfWeek == Calendar.FRIDAY) {
                    count++;
                    // Or do whatever you need to with the result.
                }
            }
        }
        return count;
    }

    // last day of the month
    private int LASTDAY(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //String date = c.getTime().toString();
        return c.get(Calendar.DATE);
    }

    public String formatNumber(String numberStr) {
        // Parse the string to a double
        double number = Double.parseDouble(numberStr);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        // Create a DecimalFormat object with the desired format
        // Format the number to always display two decimal places
        return decimalFormat.format(number);
    }


}
