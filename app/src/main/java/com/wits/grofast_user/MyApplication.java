package com.wits.grofast_user;

import android.app.Application;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    }
}
