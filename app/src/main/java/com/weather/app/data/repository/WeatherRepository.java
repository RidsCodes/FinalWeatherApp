package com.weather.app.data.repository;

import com.weather.app.data.db.dao.WeatherDao;
import com.weather.app.data.model.Forecast;
import com.weather.app.data.network.NetworkManager;
import com.weather.app.data.network.service.WeatherService;
import com.weather.app.data.model.Weather;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * This class represents a repository that handles weather data.
 */
public class WeatherRepository {

    /**
     *  The WeatherDao, WeatherService and NetworkManager dependencies.
     */
    private final WeatherDao dao;
    private final WeatherService service;
    private final NetworkManager networkManager;


    /**
     * Constructor that injects the WeatherDao, WeatherService and NetworkManager dependencies.
     */
    @Inject
    public WeatherRepository(WeatherDao dao, WeatherService service, NetworkManager networkManager) {
        this.dao = dao;
        this.service = service;
        this.networkManager = networkManager;
    }

    /**
     *  Fetches the current weather data.
     *  If there is an internet connection, data is fetched from the network. Otherwise, data is fetched from the local database.
     *  Returns a Single that emits the current weather.
     */
    public Single<Weather> fetchCurrentWeather(Double lat, Double lon) {
        if (networkManager.isInternetAvailable()) {
            return fetchCurrentWeatherFromNetwork(lat, lon);
        } else {
            return fetchCurrentWeatherFromLocal();
        }
    }

    /**
     *  Fetches the weather forecast data.
     *  If there is an internet connection, data is fetched from the network. Otherwise, data is fetched from the local database.
     *  Returns a Single that emits the weather forecast.
     */
    public Single<Forecast> fetchForecast(Double lat, Double lon) {
        if (networkManager.isInternetAvailable()) {
            return fetchForecastFromNetwork(lat, lon);
        } else {
            return fetchForecastFromLocal();
        }
    }

    /**
     *  Fetches the current weather data from the network.
     *  Returns a Single that emits the current weather and saves it to the local database.
     */
    public Single<Weather> fetchCurrentWeatherFromNetwork(Double lat, Double lon) {
        return service.fetchCurrentWeather(lat, lon)
                .map(this::insertCurrentWeather)
                .firstOrError();
    }

    /**
     * Fetches the weather forecast data from the network.
     * Returns a Single that emits the weather forecast and saves it to the local database.
     */
    public Single<Forecast> fetchForecastFromNetwork(Double lat, Double lon) {
        return service.fetchForecast(lat, lon)
                .map(this::insertForecast)
                .firstOrError();
    }


    /**
     *  Fetches the current weather data from the local database.
     *  Returns a Single that emits the current weather.
     */
    public Single<Weather> fetchCurrentWeatherFromLocal() {
        return dao.fetchWeather()
                .flatMapIterable(list -> list)
                .firstOrError();
    }

    /**
     *  Fetches the weather forecast data from the local database.
     *  Returns a Single that emits the weather forecast.
     */
    public Single<Forecast> fetchForecastFromLocal() {
        return dao.fetchForecast()
                .flatMapIterable(list -> list)
                .firstOrError();
    }


    /**
     *  Inserts the weather forecast into the local database.
     *  Deletes all previous forecast data before inserting the new data.
     *  Returns the inserted forecast.
     */
    public Forecast insertForecast(Forecast forecast) {
        dao.deleteAllForecast();
        dao.insertForecast(forecast);
        return forecast;
    }

    /**
     * Inserts the current weather data into the local database.
     * Deletes all previous weather data before inserting the new data.
     * Returns the inserted weather data.
     */
    public Weather insertCurrentWeather(Weather weather) {
        dao.deleteAll();
        dao.insert(weather);
        return weather;
    }
}
