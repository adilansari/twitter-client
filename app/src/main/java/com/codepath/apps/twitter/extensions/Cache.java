package com.codepath.apps.twitter.extensions;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Cache {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;
    private static final String CURRENT_USER_KEY = "currentUser";
    private static Cache cacheInstance;

    private Cache(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        edit = prefs.edit();
    }

    public static Cache getInstance(Context base){
        if (cacheInstance == null)
            cacheInstance= new Cache(base);

        return cacheInstance;
    }

    public void setCurrentUserId(long userId){
        edit.putLong(CURRENT_USER_KEY, 0);
        edit.apply();
    }

    public long getCurrentUserId(){
        return prefs.getLong(CURRENT_USER_KEY, 0);
    }

    public boolean hasCurentUserId(){
        return prefs.contains(CURRENT_USER_KEY);
    }
}
