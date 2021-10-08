package com.dtsoftware.pedometer;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Steps {

    @PrimaryKey
    public int date;
    public List<Integer> steps;

    public Steps(int date, List<Integer> steps) {
        this.date = date;
        this.steps = steps;
    }

    @Ignore
    public Steps(List<Integer> steps) {
        this.date = Util.getTodayKey();
        this.steps = steps;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public List<Integer> getSteps() {
        return steps;
    }

    public void setSteps(List<Integer> steps) {
        this.steps = steps;
    }


}
