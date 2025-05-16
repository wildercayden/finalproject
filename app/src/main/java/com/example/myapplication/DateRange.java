package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DateRange {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String firstDate;
    public String lastDate;

    public DateRange(String firstDate, String lastDate) {
        this.firstDate = firstDate;
        this.lastDate = lastDate;
    }
}
