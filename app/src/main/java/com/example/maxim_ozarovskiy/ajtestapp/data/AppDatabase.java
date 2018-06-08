package com.example.maxim_ozarovskiy.ajtestapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.maxim_ozarovskiy.ajtestapp.data.dao.DbModelExDao;
import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;

@Database(entities = {DbModelEx.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DbModelExDao dbModelExDao();
}