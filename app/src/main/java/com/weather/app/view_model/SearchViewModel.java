package com.weather.app.view_model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.weather.app.data.repository.LocationRepository;
import com.weather.app.data.model.City;
import com.weather.app.data.model.Response;
import com.weather.app.utills.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ridhim on 19,March,2023
 */
public class SearchViewModel extends ViewModel {

    // Injecting the LocationRepository into the ViewModel class
    private final LocationRepository repository;
    // A compositeDisposable to hold multiple disposables together.
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    // A MutableLiveData to hold response for the search query.
    private final MutableLiveData<Response<List<City>>> response = new MutableLiveData<>();
    // A SingleLiveEvent to hold the selected city.
    private final SingleLiveEvent<City> navigationEvent = new SingleLiveEvent<>();

    @Inject
    public SearchViewModel(LocationRepository repository) {
        this.repository = repository;
    }

    /**
     *Method to get the response LiveData.
     */
    public MutableLiveData<Response<List<City>>> getResponse() {
        return response;
    }

    /**
     * Method to get the navigationEvent LiveData.
     */
    public SingleLiveEvent<City> getNavigationEvent() {
        return navigationEvent;
    }

    /**
     * Method called when the search query is changed.
     */
    public void onSearchQueryChanged(String query) {
        // If query length is greater than 2 characters, fetch the cities.
        if (query.length() > 2) {
            fetchCities(query);
        }
    }

    /**
     * Method to save the selected city.
     */
    public void saveSelectedCity(City city) {
        // Saving the current city to the repository.
        repository.saveCurrentCity(city)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        // Setting the navigationEvent with the selected city.
                        navigationEvent.setValue(city);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(SearchViewModel.class.getName(), "Can't save location " + e);
                    }
                });
    }

    /**
     * Method to fetch cities from the repository.
     */
    private void fetchCities(String query) {
        Disposable subscription = repository.fetchCities(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> response.setValue(Response.loading()))
                .subscribe(
                        items -> response.setValue(Response.success(items)),
                        throwable -> response.setValue(Response.error(throwable))
                );

        compositeDisposable.add(subscription);
    }

    /**
     * Method called when the ViewModel is no longer used.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // Clearing the compositeDisposable.
        compositeDisposable.clear();
    }
}
