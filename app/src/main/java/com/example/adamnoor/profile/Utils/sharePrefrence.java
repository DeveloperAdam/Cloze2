package com.example.adamnoor.profile.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Adamnoor on 6/6/2018.
 */

public class sharePrefrence {
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static String getStr;

    public static void SetSharedPrefrence(Context context,String key,String value)
    {
        sharedPreferences = context.getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(key,value).commit();

    }

}
