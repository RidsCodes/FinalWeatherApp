package com.weather.app.di.module;

import android.content.Context;

import androidx.annotation.NonNull;

import com.weather.app.utills.Constant;
import com.weather.app.data.network.AppInterceptor;
import com.weather.app.data.network.service.LocationService;
import com.weather.app.data.network.NetworkManager;
import com.weather.app.data.network.service.WeatherService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ridhim on 12,March,2023
 */
@Module
public class NetworkModule {

    /**
     * Provides a singleton instance of the WeatherService
     */
    @Provides
    @Singleton
    public WeatherService provideWeatherService(@NonNull Retrofit retrofit) {
        // Returns the WeatherService instance created by Retrofit
        return retrofit.create(WeatherService.class);
    }

    /**
     * Provides a singleton instance of the LocationService
     */
    @Provides
    @Singleton
    public LocationService provideLocationService(@NonNull Retrofit retrofit) {
        // Returns the LocationService instance created by Retrofit
        return retrofit.create(LocationService.class);
    }

    /**
     * Provides a singleton instance of Retrofit
     */
    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client) {
        // Uses Retrofit's builder to create an instance of Retrofit
        return new Retrofit.Builder()
                .client(client) // Sets the OkHttpClient to use
                .baseUrl(Constant.BASE_URL) // Sets the base URL
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Adds a CallAdapter for RxJava
                .addConverterFactory(GsonConverterFactory.create()) // Adds a Converter for Gson
                .build();
    }

    /**
     * Provides a singleton instance of OkHttpClient
     */
    @Provides
    @Singleton
    public OkHttpClient provideHttpClient(
            AppInterceptor appInterceptor,
            HttpLoggingInterceptor httpLoggingInterceptor
    ) {
        // Uses OkHttpClient's builder to create an instance of OkHttpClient
        return new OkHttpClient.Builder()
                .connectTimeout(Constant.NETWORK_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constant.NETWORK_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(appInterceptor) // Adds the AppInterceptor for intercepting requests
                .addInterceptor(httpLoggingInterceptor) // Adds the HttpLoggingInterceptor for logging requests
                .build();
    }

    /**
     * Provides a singleton instance of AppInterceptor
     */
    @Provides
    @Singleton
    public AppInterceptor provideInterceptor() {
        return new AppInterceptor();
    }

    /**
     *  Provides a singleton instance of HttpLoggingInterceptor
     */
    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * Provides a singleton instance of NetworkManager
     */
    @Provides
    @Singleton
    public NetworkManager provideNetworkManager(Context context) {
        return new NetworkManager(context);
    }

}
