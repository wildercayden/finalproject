package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DateRangeDao {
    @Insert
    void insert(DateRange range);

    @Query("SELECT * FROM DateRange")
    List<DateRange> getAllRanges();
}
