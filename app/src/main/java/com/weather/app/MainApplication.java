package com.weather.app;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.weather.app.di.AppComponent;
import com.weather.app.di.DaggerAppComponent;



/**
 * Created by ridhim on 12,March,2023
 */
public class MainApplication  extends Application {
    public AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        createAppComponent();
        changeAppTheme();
    }

    private void createAppComponent() {
        appComponent = DaggerAppComponent.factory().create(this);
        appComponent.inject(this);
    }

    private void changeAppTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
