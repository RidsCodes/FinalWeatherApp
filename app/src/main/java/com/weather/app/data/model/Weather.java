package com.weather.app.data.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ridhim on 12,March,2023
 */
@Entity
public class Weather {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("weather")
    @Expose
    private List<Description> weather;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    @Embedded
    private Info info;
    @SerializedName("dt")
    @Expose
    private Integer date;
    @SerializedName("wind")
    @Expose
    @Embedded
    private Wind wind;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
