package com.example.unimasmove;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.splashscreen.SplashScreen;
import com.example.unimasmove.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DELAY = 2000; // Reduced to 2 seconds

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable redirectRunnable;
    private boolean shouldKeepOnScreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configure window before super.onCreate()
        configureWindowSettings();

        // Handle splash screen for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
            splashScreen.setKeepOnScreenCondition(() -> shouldKeepOnScreen);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize SessionManager safely
        try {
            SessionManager.init(this);
            final SessionManager session = SessionManager.getInstance();

            // Setup redirection
            setupRedirection(session);

            // Back press handling
            setupBackPressHandler();

        } catch (Exception e) {
            Log.e(TAG, "Initialization failed", e);
            redirectToFallbackScreen();
        }
    }

    private void configureWindowSettings() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        getWindow().setBackgroundDrawable(null);
    }

    private void setupRedirection(SessionManager session) {
        redirectRunnable = () -> {
            shouldKeepOnScreen = false; // Release splash screen
            try {
                Intent intent = createRedirectIntent(session);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                        this,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
                startActivity(intent, options.toBundle());
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Redirection failed", e);
                redirectToFallbackScreen();
            }
        };
        handler.postDelayed(redirectRunnable, SPLASH_DELAY);
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handler.removeCallbacks(redirectRunnable);
                finishAffinity();
            }
        });
    }

    private Intent createRedirectIntent(SessionManager session) {
        if (session.isLoggedIn()) {
            return new Intent(this,
                    "student".equals(session.getUserType()) ?
                            HomeActivity.class : DriverDataEntryActivity.class);
        }
        return new Intent(this, RegisterActivity.class);
    }

    private void redirectToFallbackScreen() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        shouldKeepOnScreen = false;
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Prevent leaks if activity is paused but not destroyed
        handler.removeCallbacks(redirectRunnable);
    }
}