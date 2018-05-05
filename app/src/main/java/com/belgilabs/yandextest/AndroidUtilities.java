package com.belgilabs.yandextest;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Hashtable;

public class AndroidUtilities {

    private final static String TAG = AndroidUtilities.class.getSimpleName();

    private static float density;
    private static boolean densityChecked = false;
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();

    public static int dp2px(float value) {
        if (!densityChecked) {
            throw new RuntimeException("AndroidUtilities.dp2px() called. Please first call checkDisplaySize");
        }

        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static float dpf2(float value) {
        if (value == 0) {
            return 0;
        }
        return density * value;
    }

    public static void checkDisplaySize(Context context) {
        if(!densityChecked) {
            try {
                density = context.getResources().getDisplayMetrics().density;
                densityChecked = true;
            } catch (Exception e) {
                Log.e(TAG, "Could not get display metrics because " + e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    public static Typeface getTypeface(String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(YandexTestApplication.applicationContext.getAssets(), assetPath);
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage(), e);
                    return null;
                }
            }
            return typefaceCache.get(assetPath);
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 88);
    }

}
