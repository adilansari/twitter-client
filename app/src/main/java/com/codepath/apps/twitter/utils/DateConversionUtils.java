package com.codepath.apps.twitter.utils;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateConversionUtils {

    private static SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
    private static SimpleDateFormat readableDateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
    private static SimpleDateFormat readableDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final String TAG = DateConversionUtils.class.getSimpleName();

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
        long time = d.getTime();
        String relativeTime = (String) DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        String[] tokens = relativeTime.split(" ");
        StringBuilder customRelativeTime = new StringBuilder(tokens[0]);

        if (tokens.length > 1) {
            if (tokens[1].toLowerCase().contains("mins")) customRelativeTime.append("m");
            else if (tokens[1].toLowerCase().contains("hour")) customRelativeTime.append("h");
            else if (tokens[1].toLowerCase().contains("day")) customRelativeTime.append("d");
            else if (tokens[1].toLowerCase().contains("week")) customRelativeTime.append("w");
        }

        return customRelativeTime.toString();
    }

    public static String getReadableDateTime(Date d) {
        return readableDateTimeFormat.format(d);
    }

    public static String getReadableDate(Date d) {
        return readableDateFormat.format(d);
    }
}
