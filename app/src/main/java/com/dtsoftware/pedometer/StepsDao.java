package com.dtsoftware.pedometer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StepsDao {

    @Query("SELECT * FROM Steps")
    LiveData<List<Steps>> getAllSteps();

    @Query("SELECT * FROM Steps WHERE date = :date LIMIT 1")
    LiveData<Steps> findByDateUpdated(int date);

    @Query("SELECT * FROM Steps WHERE date = :date LIMIT 1")
    Steps findByDate(int date);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Steps steps);

    @Update
    void update(Steps steps);

}
