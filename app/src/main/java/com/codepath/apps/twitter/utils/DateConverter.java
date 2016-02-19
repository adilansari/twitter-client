package com.codepath.apps.twitter.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adil on 2/18/16.
 */
public class DateConverter {

    private static SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
    private static final String TAG = DateConverter.class.getSimpleName();

    public Date setDateFromString(String date) {
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
}
