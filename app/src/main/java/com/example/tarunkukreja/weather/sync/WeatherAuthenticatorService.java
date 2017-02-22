package com.example.tarunkukreja.weather.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by tarunkukreja on 22/02/17.
 */

/**
 In order for the sync adapter framework to access your authenticator, you must create a bound Service for it. This service provides an Android binder object that allows the framework to call your authenticator and pass data between the authenticator and the framework.

 Since the framework starts this Service the first time it needs to access the authenticator, you can also use the service to instantiate the authenticator, by calling the authenticator constructor in the Service.onCreate() method of the service.

 The following snippet shows you how to define the bound Service:
 */

public class WeatherAuthenticatorService extends Service {

    private WeatherAuthenticator mWeatherAuthenticator ;
    @Override
    public void onCreate() {

        mWeatherAuthenticator = new WeatherAuthenticator(this) ;
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherAuthenticator.getIBinder();
    }
}
