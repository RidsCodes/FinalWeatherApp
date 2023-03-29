package com.weather.app.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

/**
 * Created by ridhim on 19,March,2023
 */
public class NetworkManager {
    private final Context context;

    @Inject
    public NetworkManager(Context context) {
        this.context = context;
    }

    /**
     * Check if device is connected to the internet
     * @return True if the device is connected to the internet, False otherwise
     */
    public Boolean isInternetAvailable() {
        // Get an instance of ConnectivityManager from the context
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get the active network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // Check if the network info is not null and the device is connected to the internet
        return networkInfo != null && networkInfo.isConnected();
    }

}
