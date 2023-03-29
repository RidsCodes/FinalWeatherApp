package com.weather.app.utills;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by ridhim on 19,March,2023
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelsMap;

    // The constructor takes in the viewModelsMap parameter which is injected using Dagger.
    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelsMap) {
        this.viewModelsMap = viewModelsMap;
    }

    /** The create method creates an instance of the requested ViewModel.
     * It takes in the class of the requested ViewModel and returns the instance of the ViewModel using the provider.
     * If the provider is not found in the map, it tries to find a ViewModel that is assignable to the requested class.
     * If still not found, it throws an IllegalArgumentException.
     */
    @NonNull
    @Override
    @SuppressLint("UNCHECKED_CAST")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<ViewModel> creator = viewModelsMap.get(modelClass);

        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : viewModelsMap.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}