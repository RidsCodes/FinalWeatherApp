package com.weather.app.utills;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * A utility class for loading weather icons using Glide library.
 */
public class ImageUtils {

    /**
     * Loads the weather icon with the specified icon code from the OpenWeatherMap API using Glide.
     *
     * @param imageView  the ImageView to load the icon into
     * @param iconCode   the code of the icon to load
     */
    public static void loadWeatherIcon(ImageView imageView, String iconCode) {
        if (iconCode == null) return;

        Glide.with(imageView.getContext())
                .load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
