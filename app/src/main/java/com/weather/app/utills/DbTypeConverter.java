package com.weather.app.utills;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weather.app.data.model.Description;
import com.weather.app.data.model.Weather;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ridhim on 19,March,2023
 */
public class DbTypeConverter {

    @TypeConverter
    public String fromCountryLangList(List<Description> descriptionList) {
        if (descriptionList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Description>>() {}.getType();
        String json = gson.toJson(descriptionList, type);
        return json;
    }

    @TypeConverter
    public List<Description> toCountryLangList(String description) {
        if (description == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Description>>() {}.getType();
        List<Description> descriptionList = gson.fromJson(description, type);
        return descriptionList;
    }

    @TypeConverter
    public String fromWeatherList(List<Weather> weatherList) {
        if (weatherList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Weather>>() {}.getType();
        String json = gson.toJson(weatherList, type);
        return json;
    }

    @TypeConverter
    public List<Weather> toWeatherList(String weather) {
        if (weather == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Weather>>() {}.getType();
        List<Weather> weatherList = gson.fromJson(weather, type);
        return weatherList;
    }


}
