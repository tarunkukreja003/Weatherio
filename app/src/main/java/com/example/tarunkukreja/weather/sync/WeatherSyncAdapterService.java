package com.example.tarunkukreja.weather.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by tarunkukreja on 22/02/17.
 */

public class WeatherSyncAdapterService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static WeatherSyncAdapter sSunshineSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("WeatherSyncService", "onCreate - WeatherSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new WeatherSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}
