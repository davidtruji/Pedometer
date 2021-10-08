package com.dtsoftware.pedometer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    TextView tvStepsCount, tvInfo;
    Intent intent = null;
    int todaySteps = 0;
    AppRepository repository;
    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    launchService();
                } else {
                    // Permission is denied
                    // Snackbar.make(viewPager2, getString(R.string.location_denied), BaseTransientBottomBar.LENGTH_LONG).show();
                }
            });


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateStepCount(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new AppRepository(getApplication());
        setContentView(R.layout.activity_main);
        tvStepsCount = findViewById(R.id.tvSteps);
        tvInfo = findViewById(R.id.tvInfo);
        intent = new Intent(getBaseContext(), StepsCounterService.class);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED)
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        else
            launchService();
    }


    private void launchService() {
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(StepsCounterService.BROADCAST_ACTION));
    }

    private void updateStepCount(Intent intent) {
        tvStepsCount.setText(String.valueOf(intent.getIntExtra("steps", 0)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE", "Destroy activity-------------------------------------------------");
        //stopService(intent);
    }


}