package com.example.unimasmove;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.unimasmove.workers.PredictionWorker;
import com.example.unimasmove.utils.SessionManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.concurrent.TimeUnit;

public class unimasmove extends Application {
    private static final String TAG = "UNIMASMoveApp";
    private static final String PREDICTION_WORK_TAG = "periodic_prediction";
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        // Initialize application components
        initializeFacebookSdk();
        initializeSessionManager();
        setupPeriodicPredictions();
    }

    private void initializeFacebookSdk() {
        try {
            // Configure Facebook SDK settings
            FacebookSdk.setAutoInitEnabled(true);
            FacebookSdk.setAdvertiserIDCollectionEnabled(true);
            FacebookSdk.setAutoLogAppEventsEnabled(true);

            // Initialize the SDK (auto-initialization is preferred in newer versions)
            FacebookSdk.fullyInitialize();

            // Start logging events
            AppEventsLogger.activateApp(this);

            Log.d(TAG, "Facebook SDK initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Facebook SDK initialization failed", e);
            // Consider implementing fallback analytics here
        }
    }

    private void initializeSessionManager() {
        try {
            SessionManager.init(this);
            Log.d(TAG, "SessionManager initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "SessionManager initialization failed", e);
        }
    }

    private void setupPeriodicPredictions() {
        try {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest predictionWork = new PeriodicWorkRequest.Builder(
                    PredictionWorker.class,
                    15, // Minimum interval is 15 minutes
                    TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    PREDICTION_WORK_TAG,
                    ExistingPeriodicWorkPolicy.KEEP,
                    predictionWork);

            Log.d(TAG, "Periodic predictions scheduled successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to schedule periodic predictions", e);
        }
    }

    public static Context getAppContext() {
        return appContext;
    }
}