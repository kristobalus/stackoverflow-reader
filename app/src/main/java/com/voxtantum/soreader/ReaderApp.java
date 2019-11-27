package com.voxtantum.soreader;

import android.app.Application;

import com.squareup.otto.Bus;
import com.voxtantum.soreader.api.ApiModule;
import com.voxtantum.soreader.injection.AppComponent;
import com.voxtantum.soreader.injection.DaggerAppComponent;
import com.voxtantum.soreader.loggers.DebugLogTree;
import com.voxtantum.soreader.loggers.NotLoggingTree;

import timber.log.Timber;

@SuppressWarnings("deprecation")
public class ReaderApp extends Application {

    public Bus messageBus = new Bus();
    public AppComponent applicationComponent;

    private static ReaderApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        applicationComponent = DaggerAppComponent
                .builder()
                .apiModule(new ApiModule())
                .build();

        // initialization of Timber logger
        if (BuildConfig.DEBUG)
            Timber.plant(new DebugLogTree());
        else
            Timber.plant(new NotLoggingTree());
    }

    public static AppComponent getApplicationComponent() {
        return instance.applicationComponent;
    }


}
