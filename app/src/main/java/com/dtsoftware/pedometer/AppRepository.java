package com.dtsoftware.pedometer;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class AppRepository {


    public final StepsDao stepsDao;
    private final LiveData<List<Steps>> weekelySteps;

//    private final LiveData<List<Steps>> allSteps;
//    private final LiveData<Steps> todaySteps;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        stepsDao = db.stepsDao();
        populateDB();
        weekelySteps = getBDWeekelySteps();
//        todaySteps = stepsDao.findByDate(Util.getTodayKey());
    }


    public LiveData<List<Steps>> getWeekelySteps() {
        return weekelySteps;
    }

    private LiveData<List<Steps>> getBDWeekelySteps() {
        LocalDate todayDate = LocalDate.now();
        LocalDate tomorrowDate = LocalDate.ofEpochDay(todayDate.toEpochDay() + 1);
        LocalDate date6DaysAgo = LocalDate.ofEpochDay(todayDate.toEpochDay() - 6);
        return stepsDao.findStepsBetweenDates(date6DaysAgo, tomorrowDate);
    }

    private void populateDB() {
        LocalDate todayDate = LocalDate.now();
        int stepsDef = 1000;

        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            AppDatabase.databaseWriteExecutor.execute(() -> stepsDao.insert(new Steps(todayDate.toEpochDay() -finalI, LocalDate.ofEpochDay(todayDate.toEpochDay() - finalI), Arrays.asList(finalI, stepsDef))));
        }

    }


    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
//    public LiveData<List<Steps>> getAllSteps() {
//        return allSteps;
//    }
//
//    public LiveData<Steps> getTodaySteps() {
//        return todaySteps;
//    }
//
//    public void insertSteps(Steps steps) {
//        AppDatabase.databaseWriteExecutor.execute(() -> stepsDao.insert(steps));
//    }
//
//    public void updateSteps(Steps steps) {
//        AppDatabase.databaseWriteExecutor.execute(() -> stepsDao.update(steps));
//    }

}