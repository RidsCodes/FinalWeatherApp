package com.weather.app.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.weather.app.data.model.City;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


/**
 * Created by ridhim on 19,March,2023
 */
@Dao
public interface LocationDao {

    @Query("SELECT * FROM city")
    Flowable<List<City>> fetchCities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(City city);
}
