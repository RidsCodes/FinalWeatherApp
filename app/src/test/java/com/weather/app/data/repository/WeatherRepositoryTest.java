package com.weather.app.data.repository;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.weather.app.data.db.dao.WeatherDao;
import com.weather.app.data.model.Forecast;
import com.weather.app.data.model.Weather;
import com.weather.app.data.network.service.WeatherService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.observers.TestObserver;

/**
 * Created by ridhim on 23,March,2023
 */
@RunWith(MockitoJUnitRunner.class)
public class WeatherRepositoryTest {

    @Mock
    private WeatherDao dao;

    @Mock
    private WeatherService service;

    @InjectMocks
    private WeatherRepository repository;

    @Test
    public void testFetchCurrentWeatherFromNetwork() {
        // Create test data
        Double lat = 12.34;
        Double lon = 56.78;
        Weather weather = new Weather();

        // Set up mock behavior
        when(service.fetchCurrentWeather(lat, lon)).thenReturn(Flowable.just(weather));

        // Call the method being tested
        TestObserver<Weather> testObserver = repository.fetchCurrentWeatherFromNetwork(lat, lon).test();

        // Verify the results
        testObserver.assertValue(weather);

    }

    @Test
    public void testFetchCurrentWeatherFromLocal() {
        // Create test data
        Weather weather = new Weather();
        List<Weather> weatherList = Collections.singletonList(weather);

        // Set up mock behavior
        when(dao.fetchWeather()).thenReturn(Flowable.just(weatherList));

        // Call the method being tested
        TestObserver<Weather> testObserver = repository.fetchCurrentWeatherFromLocal().test();

        // Verify the results
        testObserver.assertValue(weather);
        verifyZeroInteractions(service);
    }

    @Test
    public void testFetchForecastFromNetwork() {
        // Create test data
        Double lat = 12.34;
        Double lon = 56.78;
        Forecast forecast = new Forecast();

        // Set up mock behavior
        when(service.fetchForecast(lat, lon)).thenReturn(Flowable.just(forecast));

        // Call the method being tested
        TestObserver<Forecast> testObserver = repository.fetchForecastFromNetwork(lat, lon).test();

        // Verify the results
        testObserver.assertValue(forecast);
    }

    @Test
    public void testFetchForecastFromLocal() {
        // Create test data
        Forecast forecast = new Forecast();
        List<Forecast> forecastList = Collections.singletonList(forecast);

        // Set up mock behavior
        when(dao.fetchForecast()).thenReturn(Flowable.just(forecastList));

        // Call the method being tested
        TestObserver<Forecast> testObserver = repository.fetchForecastFromLocal().test();

        // Verify the results
        testObserver.assertValue(forecast);
        verifyZeroInteractions(service);
    }
}