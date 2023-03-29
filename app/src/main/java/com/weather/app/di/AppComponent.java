package com.weather.app.di;

import android.content.Context;

import com.weather.app.MainApplication;
import com.weather.app.di.module.DatabaseModule;
import com.weather.app.di.module.NetworkModule;
import com.weather.app.di.module.RepositoryModule;
import com.weather.app.di.module.ViewModelModule;
import com.weather.app.ui.MainActivity;
import com.weather.app.ui.SearchDialog;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by ridhim on 12,March,2023
 */
@Singleton
@Component(modules = {
        NetworkModule.class,
        DatabaseModule.class,
        ViewModelModule.class,
        RepositoryModule.class
})
public interface AppComponent {

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Context context);
    }

    void inject(MainActivity activity);

    void inject(MainApplication application);

    void inject(SearchDialog dialog);
}
