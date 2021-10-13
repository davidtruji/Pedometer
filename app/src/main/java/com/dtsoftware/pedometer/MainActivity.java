package com.dtsoftware.pedometer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dtsoftware.pedometer.ui.dashboard.DashboardFragment;
import com.dtsoftware.pedometer.ui.home.HomeFragment;
import com.dtsoftware.pedometer.ui.home.HomeViewModel;
import com.dtsoftware.pedometer.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    Intent intent = null;
    BottomNavigationView bottomNavigationView;
    AppRepository repository;
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
        setContentView(R.layout.activity_main);
        intent = new Intent(getBaseContext(), StepsCounterService.class);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    openFragment(fragment);
                    break;
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    openFragment(fragment);
                    break;
                case R.id.navigation_notifications:
                    fragment = new NotificationsFragment();
                    openFragment(fragment);
                    break;
            }

            return true;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED)
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        else
            launchService();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }


    private void launchService() {
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(StepsCounterService.BROADCAST_ACTION));
    }

    private void updateStepCount(Intent intent) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        int step =intent.getIntExtra("steps", 0);
        homeViewModel.setSteps(step);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE", "Destroy activity");
    }


}