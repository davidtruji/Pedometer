package com.dtsoftware.pedometer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Dao
public interface StepsDao {

    //TODO: Crear más y mejores querys
    @Query("SELECT * FROM Steps")
    LiveData<List<Steps>> getAllSteps();

    @Query("SELECT * FROM Steps WHERE date_key = :date LIMIT 1")
    LiveData<Steps> findByDateUpdated(LocalDate date);

    @Query("SELECT * FROM Steps WHERE date_key = :date LIMIT 1")
    Steps findByDate(Long date);

    @Query("SELECT * FROM Steps WHERE date BETWEEN :from AND :to ORDER BY date_key ASC")
    LiveData<List<Steps>> findStepsBetweenDates(LocalDate from, LocalDate to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Steps steps);

    @Update
    void update(Steps steps);

}
