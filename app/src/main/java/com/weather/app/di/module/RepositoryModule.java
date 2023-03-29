package com.weather.app.di.module;

import com.weather.app.data.db.dao.LocationDao;
import com.weather.app.data.network.service.LocationService;
import com.weather.app.data.network.NetworkManager;
import com.weather.app.data.repository.LocationRepository;
import com.weather.app.data.repository.WeatherRepository;
import com.weather.app.data.db.dao.WeatherDao;
import com.weather.app.data.network.service.WeatherService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ridhim on 19,March,2023
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    WeatherRepository provideWeatherRepository(WeatherDao dao, WeatherService service, NetworkManager networkManager) {
        return new WeatherRepository(dao, service, networkManager);
    }

    @Provides
    @Singleton
    LocationRepository provideLocationRepository(LocationDao dao, LocationService service) {
        return new LocationRepository(dao, service);
    }
}
