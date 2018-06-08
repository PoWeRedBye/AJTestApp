package com.example.maxim_ozarovskiy.ajtestapp.data;

import android.app.Application;
import android.arch.persistence.room.Room;

public class Database extends Application {

    public static Database instance;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
    }

    public static Database getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }



}



