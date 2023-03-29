package com.weather.app.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.weather.app.MainApplication;
import com.weather.app.ui.adapter.ForecastAdapter;
import com.weather.app.utills.ImageUtils;
import com.weather.app.view_model.MainViewModel;
import com.weather.app.data.model.City;
import com.weather.app.data.model.DailyForecast;
import com.weather.app.data.model.Weather;
import com.weather.app.data.model.Description;
import com.weather.app.data.model.Response;
import com.weather.app.databinding.ActivityMainBinding;
import com.weather.app.utills.DataUtils;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements SearchDialog.SearchDialogCallback {
    // Data binding object for the activity's layout
    private ActivityMainBinding binding;

    // Dependency injection objects
    @Inject
    ViewModelProvider.Factory factory;
    // View model for the activity
    private MainViewModel viewModel;
    // Adapter for the RecyclerView
    private ForecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectActivity();
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create the view model, adapter, set listeners, and subscribe to data
        createViewModel();
        initAdapter();
        setListeners();
        subscribeToData();

        // Check if there is a saved location and update the view model if there is
        viewModel.checkSavedLocation();
    }

    /**
     * Initialization methods
     */
    //Inject the activity with dependencies
    private void injectActivity() {
        ((MainApplication) getApplication()).appComponent.inject(this);
    }

    /**
     * Create the view model using the ViewModelProvider and the factory
     */
    private void createViewModel() {
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
    }

    /**
     * Initialize the adapter and set it to the RecyclerView
     */
    private void initAdapter() {
        adapter = new ForecastAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    /**
     * Set listeners for the search view
     */
    private void setListeners() {
        binding.serchView.setOnClickListener(view -> openSearchDialogFragment());
    }

    /**
     * Subscribe to the LiveData objects in the view model
     */
    private void subscribeToData() {
        viewModel.getWeather().observe(this, this::checkWeatherResponse);

        viewModel.getCity().observe(this, this::showLocation);

        viewModel.getForecast().observe(this, this::checkForecastResponse);
    }


    /**
     * Check the Weather response and update the UI accordingly
     */
    private void checkWeatherResponse(Response<Weather> response) {
        switch (response.status) {
            case LOADING:
                renderLoadingState();
                break;
            case SUCCESS:
                renderSuccessState();
                showWeatherData(response.data);
                break;
            case ERROR:
                renderErrorState();
                showErrorMsg();
                break;
        }
    }

    /**
     * Check the forecast response and update the UI accordingly
     */
    private void checkForecastResponse(Response<List<DailyForecast>> response) {
        switch (response.status) {
            case LOADING:
                renderLoadingState();
                break;
            case SUCCESS:
                renderSuccessState();
                showForecastData(response.data);
                break;
            case ERROR:
                renderErrorState();
                showErrorMsg();
                break;
        }
    }


    /**
     * Show the loading state UI
     */
    private void renderLoadingState() {
        binding.messageGroup.setVisibility(View.GONE);
        binding.weatherGroup.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
    }

    /**
     * Show the success state UI
     */
    private void renderSuccessState() {
        binding.messageGroup.setVisibility(View.GONE);
        binding.weatherGroup.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.GONE);
    }

    /**
     * Show the error state UI
     */
    private void renderErrorState() {
        binding.messageGroup.setVisibility(View.VISIBLE);
        binding.weatherGroup.setVisibility(View.GONE);
        binding.progress.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
    }

    /**
     * Method to show daily forecast data on screen.
     *
     * @param items a list of daily forecast items to display
     */
    private void showForecastData(List<DailyForecast> items) {
        // If the list is null or empty, return without displaying anything
        if (items == null || items.isEmpty()) {
            return;
        }
        // Set the list of items on the adapter to be displayed
        adapter.setItems(items);
    }

    /**
     * Method to show current weather data on screen.
     *
     * @param weather the weather object containing weather information to display
     */
    @SuppressLint("SetTextI18n")
    private void showWeatherData(Weather weather) {
        // Format the temperature, minimum temperature, maximum temperature, and feels-like temperature strings
        String temp = DataUtils.formatKelvinToCelsius(weather.getInfo().getTemp());
        String minTemp = DataUtils.formatKelvinToCelsius(weather.getInfo().getTempMin());
        String maxTemp = DataUtils.formatKelvinToCelsius(weather.getInfo().getTempMax());
        String feelLikeTemp = DataUtils.formatKelvinToCelsius(weather.getInfo().getFeelsLike());
        String minMaxStr = "Min " + minTemp + ", Max" + maxTemp;
        String feelLikeStr = "Feel like " + feelLikeTemp;
        Description description = weather.getWeather().get(0);

        // Set the temperature, minimum and maximum temperature,
        // feels-like temperature, and weather description on their respective TextViews

        binding.tempTv.setText(temp);
        binding.minMaxTempTv.setText(minMaxStr);
        binding.feelTempTv.setText(feelLikeStr);
        binding.deskTv.setText(description.getDescription());

        // Load the weather icon into the ImageView using ImageUtils
        ImageUtils.loadWeatherIcon(binding.iconIv, description.getIcon());
    }

    /**
     * Method to show location information on screen.
     *
     * @param city the city object containing location information to display
     */
    @SuppressLint("SetTextI18n")
    private void showLocation(City city) {
        // Set the location text to the city name and country
        binding.locationTv.setText(city.getName() + ", " + city.getCountry());
    }

    /**
     * Method to show an error message using a Snackbar.
     */
    private void showErrorMsg() {
        Snackbar.make(binding.getRoot(), "Can't load data", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Method to show an error message using a Snackbar.
     */
    private void openSearchDialogFragment() {
        SearchDialog dialog = new SearchDialog();
        dialog.show(getSupportFragmentManager(), SearchDialog.class.getName());
    }

    @Override
    public void onCitySelected(City city) {
        viewModel.updateCity(city);
    }
}