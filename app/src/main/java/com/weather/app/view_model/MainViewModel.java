package com.weather.app.view_model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.weather.app.data.model.City;
import com.weather.app.data.model.DailyForecast;
import com.weather.app.data.model.Weather;
import com.weather.app.data.model.Response;
import com.weather.app.data.repository.LocationRepository;
import com.weather.app.data.repository.WeatherRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ridhim on 19,March,2023
 */
public class MainViewModel extends ViewModel {

    // Repository dependencies
    private final WeatherRepository weatherRepository;
    private final LocationRepository locationRepository;
    // Disposable to handle RxJava subscriptions
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    // MutableLiveData objects to hold the current weather, forecast and selected city
    private final MutableLiveData<Response<Weather>> weather = new MutableLiveData<>();
    private final MutableLiveData<Response<List<DailyForecast>>> forecast = new MutableLiveData<>();
    private final MutableLiveData<City> city = new MutableLiveData<>();

    /**
     * Constructor injecting dependencies using Dagger
     */
    @Inject
    public MainViewModel(WeatherRepository weatherRepository, LocationRepository locationRepository) {
        this.weatherRepository = weatherRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Getters for the MutableLiveData objects
     */
    public MutableLiveData<Response<Weather>> getWeather() {
        return weather;
    }

    public MutableLiveData<Response<List<DailyForecast>>> getForecast() {
        return forecast;
    }

    public MutableLiveData<City> getCity() {
        return city;
    }


    /**
     * Methods to work with location
     */
    // Update the selected city and fetch the weather
    public void updateCity(City city) {
        this.city.setValue(city);
        fetchWeather();
    }

    /**
     * Check if a saved city exists,
     * if not fetch the current location and then fetch the weather
     */
    public void checkSavedLocation() {
        if (city.getValue() == null) {
            fetchCurrentLocation();
        } else {
            fetchWeather();
        }
    }

    /**
     * Fetch the saved city from the location repository
     */
    private void fetchCurrentLocation() {
        Disposable subscription = locationRepository.fetchSavedCity()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onCityItemsFetched,
                        throwable -> Log.d("ERROR", throwable.getMessage())
                );

        compositeDisposable.add(subscription);
    }

    /**
     * Update the selected city with the first saved city and fetch the weather
     */
    private void onCityItemsFetched(List<City> items) {
        if (items.isEmpty()) {
            return;
        }

        updateCity(items.get(0));
    }


    /**
     * Fetch the current weather for the selected city using the weather repository
     */
    private void fetchWeather() {
        City city = this.city.getValue();
        Double lat = city.getLat();
        Double lon = city.getLon();

        fetchCurrentWeather(lat, lon);
        fetchForecast(lat, lon);
    }

    /**
     * Fetch the current weather for the selected city using the weather repository
     */
    private void fetchCurrentWeather(Double lat, Double lon) {
        Disposable subscription = weatherRepository.fetchCurrentWeather(lat, lon)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> weather.setValue(Response.loading()))
                .subscribe(
                        item -> weather.setValue(Response.success(item)),
                        throwable -> weather.setValue(Response.error(throwable))
                );

        compositeDisposable.add(subscription);
    }


    /**
     * Methods to fetch and filter forecast data
     */
    private void fetchForecast(Double lat, Double lon) {
        Disposable subscription = weatherRepository.fetchForecast(lat, lon)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> forecast.setValue(Response.loading()))
                .subscribe(
                        item -> filterDailyForecast(item.getList()),
                        throwable -> forecast.setValue(Response.error(throwable))
                );

        compositeDisposable.add(subscription);
    }

    /**
     * Filters a list of weather items to get a list of unique daily forecasts
     *
     * @param items the list of weather items to filter
     */
    private void filterDailyForecast(List<Weather> items) {
        // Create a new list to store the filtered daily forecasts
        List<DailyForecast> filteredItems = new ArrayList<>();

        // Loop through each weather item in the original list
        for (Weather item : items) {
            // Get the day of the week for the current weather item
            int dayOfWeek = dayOfWeek(item.getDate());

            // Flag to track if a matching day has already been added to the filtered list
            boolean isNoExist = true;

            // Loop through each existing daily forecast in the filtered list
            for (DailyForecast forecast : filteredItems) {
                // Get the day of the week for the existing forecast
                int filteredDay = dayOfWeek(forecast.getDate());

                // If the day of the week for the current weather item matches a day
                // already in the filtered list, set the flag to false
                if (dayOfWeek == filteredDay) {
                    isNoExist = false;
                }
            }

            // If no matching day was found in the filtered list,
            // add a new daily forecast to the list based on the current weather item
            if (isNoExist) {
                filteredItems.add(convertWeatherToDailyForecast(item));
            }
        }

        filterHourlyForecast(items, filteredItems);
    }

    /**
     * Filters a list of weather items to get a list of unique hourly forecasts
     *
     * @param items      the list of weather items to filter
     * @param dailyItems the list of daily forecast
     */
    private void filterHourlyForecast(List<Weather> items, List<DailyForecast> dailyItems) {
        for (DailyForecast daily : dailyItems) {
            // Get the day of the week for the current daily forecast
            int dailyDay = dayOfWeek(daily.getDate());

            // Create a new list to hold the hourly forecast for this day
            List<Weather> hourlyForecast = new ArrayList<>();

            // Iterate over all the weather items
            for (Weather item : items) {
                // Get the day of the week for the current weather item
                int day = dayOfWeek(item.getDate());

                // If the day of the week for the weather item matches the day of the week
                // for the current daily forecast, add it to the hourly forecast list
                if (dailyDay == day) {
                    hourlyForecast.add(item);
                }
            }

            // Set the hourly forecast list for the current daily forecast
            daily.setHourlyForecast(hourlyForecast);
        }

        // Set the filtered daily forecast list as the response value
        forecast.setValue(Response.success(dailyItems));
    }


    /**
     * Method to convert weather ro daily forecast
     */
    private DailyForecast convertWeatherToDailyForecast(Weather weather) {
        DailyForecast forecast = new DailyForecast();

        forecast.setBase(weather.getBase());
        forecast.setDate(weather.getDate());
        forecast.setInfo(weather.getInfo());
        forecast.setWeather(weather.getWeather());
        forecast.setWind(weather.getWind());

        return forecast;
    }

    /**
     * Converts milliseconds to a day of the week (represented as an integer).
     *
     * @param mill The number of milliseconds since the epoch (January 1, 1970, 00:00:00 UTC).
     * @return An integer representing the day of the Month.
     */
    private int dayOfWeek(int mill) {
        // Convert milliseconds to a Date object
        long timeInMills = mill * 1000L;
        Date date = new Date(timeInMills);

        // Create a Calendar object and set its time to the specified Date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Return the day of the month
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
    }
}
