package com.dtsoftware.pedometer;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class Steps {

    //TODO Crear POJO que funcione en condiciones para busquedas por fechas

    @PrimaryKey
    public Long date_key;
    public LocalDate date;
    public List<Integer> steps;

    public Steps(Long date_key, LocalDate date, List<Integer> steps) {
        this.date_key = date_key;
        this.date = date;
        this.steps = steps;
    }

    @Ignore
    public Steps(List<Integer> steps) {
        this.date_key = LocalDate.now().toEpochDay();
        this.date = LocalDate.now();
        this.steps = steps;
    }


    public Long getDate_key() {
        return date_key;
    }

    public void setDate_key(Long date_key) {
        this.date_key = date_key;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Integer> getSteps() {
        return steps;
    }

    public void setSteps(List<Integer> steps) {
        this.steps = steps;
    }

    public int getTotalSteps() {
        int totalSteps = 0;

        for (int i : steps)
            totalSteps += i;

        return totalSteps;
    }
}
