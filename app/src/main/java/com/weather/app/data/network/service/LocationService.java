package com.weather.app.data.network.service;

import com.weather.app.data.model.City;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ridhim on 19,March,2023
 */
public interface LocationService {

    @GET("geo/1.0/direct?limit=5")
    Flowable<List<City>> fetchCities(@Query("q") String query);
}
