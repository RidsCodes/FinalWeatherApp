package com.weather.app.data.repository;

import com.weather.app.data.db.dao.LocationDao;
import com.weather.app.data.network.service.LocationService;
import com.weather.app.data.model.City;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * This class represents a repository that handles location data.
 */
public class LocationRepository {

    /**
     * The LocationDao and LocationService dependencies.
     */
    private final LocationDao dao;
    private final LocationService service;

    /**
     * Constructor that injects the LocationDao and LocationService dependencies.
     */
    @Inject
    public LocationRepository(LocationDao dao, LocationService service) {
        this.dao = dao;
        this.service = service;
    }

    /**
     * Fetches a list of cities from the LocationService API based on the provided query.
     * Returns a Single that emits a list of cities.
     */
    public Single<List<City>> fetchCities(String query) {
        return service.fetchCities(query)
                .firstOrError();
    }

    /**
     * Fetches a list of saved cities from the local database.
     * Returns a Single that emits a list of cities.
     */
    public Single<List<City>> fetchSavedCity() {
        return dao.fetchCities()
                .firstOrError();
    }

    /**
     * Inserts the provided city into the local database.
     * Returns a Completable that completes when the city is saved.
     */
    public Completable saveCurrentCity(City city) {
        return dao.insert(city);
    }
}
