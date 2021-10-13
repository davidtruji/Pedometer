package com.dtsoftware.pedometer.ui.dashboard;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dtsoftware.pedometer.AppRepository;
import com.dtsoftware.pedometer.Steps;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<Steps>> weeklySteps;

    public DashboardViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        weeklySteps = repository.getWeekelySteps();
    }

    public LiveData<List<Steps>> getWeekelySteps() {
        return weeklySteps;
    }

}