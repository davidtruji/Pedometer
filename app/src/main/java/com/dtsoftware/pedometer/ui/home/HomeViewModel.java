package com.dtsoftware.pedometer.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> steps;

    public HomeViewModel(Application application) {
        super(application);
        steps = new MutableLiveData<>();
    }

    public LiveData<Integer> getText() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps.setValue(steps);
    }
}