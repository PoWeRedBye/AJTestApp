package com.example.maxim_ozarovskiy.ajtestapp.data.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Dao
public interface DbModelExDao {

    @Query("SELECT * FROM dbmodelex")
    Flowable<List<DbModelEx>> getAll();

    @Query("SELECT * FROM dbmodelex WHERE id = :id")
    DbModelEx getById(long id);

    @Insert
    void insert(DbModelEx model);

    @Update
    void update(DbModelEx model);

    @Delete
    void delete(DbModelEx model);

}
