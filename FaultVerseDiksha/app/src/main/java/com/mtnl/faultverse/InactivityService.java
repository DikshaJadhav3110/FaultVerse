package com.mtnl.faultverse;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

// InactivityService.java
public class InactivityService extends Service {
    private static final long THIRTY_SECONDS = 30 * 1000; // 30 seconds in milliseconds

    private Handler inactivityHandler;
    private Runnable inactivityRunnable;
    private long lastInteractionTime;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        inactivityHandler = new Handler();
        inactivityRunnable = new Runnable() {
            @Override
            public void run() {
                logoutUser();
            }
        };

        startInactivityMonitoring();
        return START_STICKY;
    }

    private void startInactivityMonitoring() {
        inactivityHandler.postDelayed(inactivityRunnable, THIRTY_SECONDS);
    }

    private void resetInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable);
        inactivityHandler.postDelayed(inactivityRunnable, THIRTY_SECONDS);
        lastInteractionTime = System.currentTimeMillis();
    }

    private void logoutUser() {
        // Perform logout actions here, e.g., clear user session or show logout message
        // ...

        // Stop the service once the user is logged out
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // Stop the service if the app is removed from the recent tasks
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
