package com.dtsoftware.pedometer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

public class StepsCounterService extends Service implements SensorEventListener {

    private final String TAG = "STEP_SERVICE";
    public static final String BROADCAST_ACTION = "XXX.Counter";

    private final Handler handler = new Handler();
    private boolean bdInitialized;
    private Intent intent;
    private int prevCount, currentCount, dayCount;
    private SensorManager sensorManager = null;
    private Notification notification;
    private NotificationManager notificationManager;
    private StepsDao stepsDao;
    private final Runnable updateBroadcastData = new Runnable() {
        public void run() {
            broadcastSteps();
            // Call "handler.postDelayed" again, after a specified delay.
            handler.postDelayed(this, 5000);
        }
    };


    private final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Pedometer")
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SERVICE CREATED");
        intent = new Intent(BROADCAST_ACTION);
        stepsDao = AppDatabase.getDatabase(getApplication()).stepsDao();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.unregisterListener(this);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        prevCount = -1; // Initialize value
        currentCount = 0;
        dayCount = 0;
        bdInitialized = false;
        initializeDB();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "SERVICE STARTED");
        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData);
        generateNotification();
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int lastCount = (int) event.values[0];

        if (prevCount == -1)
            prevCount = lastCount;

        currentCount = lastCount - prevCount;
        Log.d(TAG, "Sensor return:" + lastCount + " Saved value:" + currentCount);
    }


    /**
     * Called when The service is no longer used and is being destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        Log.d(TAG, "SERVICE STOPPED");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void generateNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, StepsCounterService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        notification = builder.build();

        // Notification ID cannot be 0.
        startForeground(288, notification);
    }

    private void broadcastSteps() {
        intent.putExtra("steps", currentCount + dayCount);
        // call sendBroadcast with that intent  - which sends a message to whoever is registered to receive it.
        sendBroadcast(intent);
        updateNotification();
        saveStepsOnDB();
        Log.d(TAG, "Send steps counter " + currentCount);
    }

    private void updateNotification() {
        builder.setContentText(String.valueOf(currentCount + dayCount));
        notificationManager.notify(288, builder.build());
    }

    private void initializeDB() {

        new Thread(() -> {
            Steps steps = stepsDao.findByDate(Util.getTodayKey());

            if (steps != null) {
                steps.getSteps().add(0);
                stepsDao.update(steps);

                for (int counter : steps.getSteps())
                    dayCount += counter;

            } else {
                List<Integer> stepsList = new ArrayList<>();
                stepsList.add(0);
                stepsDao.insert(new Steps(stepsList));
                dayCount = 0;
            }

            bdInitialized = true;
            Log.d(TAG, "BD has been initialized");

        }).start();

    }

    private void saveStepsOnDB() {

        if (bdInitialized) {
            new Thread(() -> {
                Steps steps = stepsDao.findByDate(Util.getTodayKey());

                if (steps != null) {
                    steps.getSteps().set(steps.getSteps().size() - 1, currentCount);
                    stepsDao.update(steps);
                } else {
                    prevCount = -1;
                    initializeDB();
                }

                Log.d(TAG, "Saved steps in BD");
            }).start();
        }

    }


}
