package com.weather.app.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.weather.app.data.db.dao.LocationDao;
import com.weather.app.data.db.dao.WeatherDao;
import com.weather.app.data.model.City;
import com.weather.app.data.model.Forecast;
import com.weather.app.data.model.Weather;
import com.weather.app.utills.DbTypeConverter;

/**
 * Created by ridhim on 12,March,2023
 */
@Database(entities = {
        City.class,
        Weather.class,
        Forecast.class
}, version = 1, exportSchema = false)
@TypeConverters(DbTypeConverter.class)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();

    public abstract LocationDao locationDao();
}
