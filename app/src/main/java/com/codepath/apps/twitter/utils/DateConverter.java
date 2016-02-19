package com.codepath.apps.twitter.utils;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
    private static final String TAG = DateConverter.class.getSimpleName();

    public static Date getDateFromString(String date) {
        sf.setLenient(true);
        Date d = new Date();
        try {
            d = sf.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "String to date conversion failed.");
            e.printStackTrace();
        }
        return d;
    }

    public static String getRelativeTimeStamp(Date d) {
        long dateMillis = d.getTime();
        String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }
}
