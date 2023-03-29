package com.weather.app.utills;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ridhim on 19,March,2023
 */
public class DataUtils {
    /**
     * Converts temperature in Kelvin to Celsius
     * and formats it to two decimal places with the degree Celsius symbol
     */
    public static String formatKelvinToCelsius(double temperature) {
        return String.format(Locale.ENGLISH, "%.0f*C", (temperature - 273.15));
    }

    /**
     * Formats humidity as a percentage
     */
    public static String formatHumidity(int humidity) {
        return humidity + "%";
    }

    /**
     * Returns the date in the "dd-MM" format based on the provided time in milliseconds
     */
    public static String getDate(long timeInMillis) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timeInMillis * 1000);
        return DateFormat.format("dd-MM", cal).toString();
    }

    /**
     * Returns the time in the "kk-mm" format based on the provided time in milliseconds
     */
    public static String getTime(long timeInMillis) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timeInMillis * 1000);
        return DateFormat.format("kk-mm", cal).toString();
    }
}
