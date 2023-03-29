package com.weather.app.di.module;

import android.content.Context;

import androidx.room.Room;

import com.weather.app.utills.Constant;
import com.weather.app.data.db.dao.LocationDao;
import com.weather.app.data.db.dao.WeatherDao;
import com.weather.app.data.db.WeatherDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ridhim on 12,March,2023
 */
@Module
public class DatabaseModule {

    /**
     *  Provides a singleton instance of the WeatherDatabase using Room
     */
    @Provides
    @Singleton
    public WeatherDatabase provideDatabase(Context context) {
        // Use Room's database builder to create a WeatherDatabase instance
        return Room.databaseBuilder(context, WeatherDatabase.class, Constant.DATABASE_NAME)
                // Specifies to fallback to destructive migration if the database schema changes
                .fallbackToDestructiveMigration()
                // Builds the WeatherDatabase instance
                .build();
    }

    /**
     * Provides a singleton instance of the WeatherDao
     */
    @Provides
    @Singleton
    public WeatherDao provideWeatherDao(WeatherDatabase db) {
        return db.weatherDao();
    }

    /**
     * Provides a singleton instance of the LocationDao
     */
    @Provides
    @Singleton
    public LocationDao provideLocationDao(WeatherDatabase db) {
        return db.locationDao();
    }
}
