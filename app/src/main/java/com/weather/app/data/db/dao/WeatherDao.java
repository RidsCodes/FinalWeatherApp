package com.weather.app.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.weather.app.data.model.Forecast;
import com.weather.app.data.model.Weather;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by ridhim on 12,March,2023
 */
@Dao
public interface WeatherDao {

    @Query("SELECT * FROM Weather")
    Flowable<List<Weather>> fetchWeather();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weather weather);

    @Query("DELETE FROM Weather")
    void deleteAll();

    @Query("SELECT * FROM Forecast")
    Flowable<List<Forecast>> fetchForecast();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecast(Forecast forecast);

    @Query("DELETE FROM Forecast")
    void deleteAllForecast();

}
