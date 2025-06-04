package com.example.myapplication;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {
    private static DatabaseClient instance;
    private final AppDatabase database;

    private DatabaseClient(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "dates-db").build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
