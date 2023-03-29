package com.weather.app.data.network.service;

import com.weather.app.data.model.Forecast;
import com.weather.app.data.model.Weather;


import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ridhim on 12,March,2023
 */
public interface WeatherService {

    @GET("data/2.5/forecast")
    Flowable<Forecast> fetchForecast(
            @Query("lat") Double lat,
            @Query("lon") Double lon
    );

    @GET("data/2.5/weather")
    Flowable<Weather> fetchCurrentWeather(
            @Query("lat") Double lat,
            @Query("lon") Double lon
    );
}
