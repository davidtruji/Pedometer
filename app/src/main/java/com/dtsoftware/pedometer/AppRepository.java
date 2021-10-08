package com.dtsoftware.pedometer;

import android.app.Application;


public class AppRepository {


//    public final StepsDao stepsDao;
//    private final LiveData<List<Steps>> allSteps;
//    private final LiveData<Steps> todaySteps;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public AppRepository(Application application) {
//        AppDatabase db = AppDatabase.getDatabase(application);
//        stepsDao = db.stepsDao();
//        allSteps = stepsDao.getAllSteps();
//        todaySteps = stepsDao.findByDate(Util.getTodayKey());
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