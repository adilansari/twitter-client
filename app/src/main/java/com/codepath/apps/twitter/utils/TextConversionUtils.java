package com.codepath.apps.twitter.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextConversionUtils {

    public static String screenName(String screenName){
        StringBuilder builder = new StringBuilder("@");
        builder.append(screenName);
        return builder.toString();
    }

    public static SpannableString linkify(String text){
        SpannableString spannableString = new SpannableString(text);
        Matcher matcher = Pattern.compile("[#@]([A-Za-z0-9_-]+)").matcher(spannableString);
        while (matcher.find())
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("teal")), matcher.start(), matcher.end(), 0);

        return spannableString;
    }
}
