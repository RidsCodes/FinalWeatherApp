package com.weather.app.data.repository;


import com.google.gson.Gson;
import com.weather.app.data.db.dao.LocationDao;
import com.weather.app.data.model.City;
import com.weather.app.data.network.service.LocationService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.observers.TestObserver;

/**
 * Created by ridhim on 23,March,2023
 */
public class LocationRepositoryTest {

    private LocationDao dao = Mockito.mock(LocationDao.class);
    private LocationService service = Mockito.mock(LocationService.class);
    private LocationRepository repository = new LocationRepository(dao, service);

    private List<City> items;

    @Before
    public void setUp()  {
        String fixture = readFixture("City.json");
        City[] response =  new Gson().fromJson(fixture, City[].class);
        items = Arrays.asList(response);
    }

    @Test
    public void fetchCities() {
        // Mock the service to return the list of cities when fetchCities is called
        Mockito.when(service.fetchCities("query")).thenReturn(Flowable.just(items));
        // Create a TestObserver to subscribe to the repository's fetchCities method
        TestObserver<List<City>> observer = new TestObserver<>();
        repository.fetchCities("query").subscribe(observer);
        // Verify that the observer received the correct list of cities
        observer.assertValue(items);
        observer.assertComplete();
    }

    @Test
    public void fetchSavedCity() {
        // Mock the dao to return the list of cities when fetchCities is called
        Mockito.when(dao.fetchCities()).thenReturn(Flowable.just(items));
        // Create a TestObserver to subscribe to the repository's fetchSavedCity method
        TestObserver<List<City>> observer = new TestObserver<>();
        repository.fetchSavedCity().subscribe(observer);
        // Verify that the observer received the correct list of cities
        observer.assertValue(items);
        observer.assertComplete();
    }

    @Test
    public void saveCurrentCity() {
        City city = items.get(0);
        // Mock the dao to return a Completable complete signal when insert is called
        Mockito.when(dao.insert(city)).thenReturn(Completable.complete());
        // Create a TestObserver to subscribe to the repository's saveCurrentCity method
        TestObserver<Void> observer = new TestObserver<>();
        repository.saveCurrentCity(city).subscribe(observer);
        // Verify that the observer received a complete signal
        observer.assertComplete();
    }

    protected String readFixture(String fileName) {

        String json;
        try {
            InputStream is = Objects.requireNonNull(this.getClass().getClassLoader())
                    .getResourceAsStream(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);

        } catch (IOException ignored) {
            return "";
        }

        return json;
    }
}