package com.weather.app.data.model;

import java.util.List;

/**
 * Created by ridhim on 19,March,2023
 */
public class DailyForecast {
    private List<Description> weather;
    private String base;
    private Info info;
    private Integer date;
    private Wind wind;
    private List<Weather> hourlyForecast;

    public List<Description> getWeather() {
        return weather;
    }

    public void setWeather(List<Description> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public List<Weather> getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(List<Weather> hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }
}
