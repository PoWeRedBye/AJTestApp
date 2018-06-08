package com.example.maxim_ozarovskiy.ajtestapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */
@Entity
public class DbModelEx {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "base_name")
    private String baseCurrencyName;

    @ColumnInfo(name = "base_value")
    private String baseCurrencyValue;

    @ColumnInfo(name = "target_name")
    private String targetCurrencyName;

    @ColumnInfo(name = "target_value")
    private String targetCurrencyValue;

    @ColumnInfo(name = "target_course")
    private String targetCurrencyCourse;

    @ColumnInfo(name = "time")
    private String dayTimeRequest;

    @ColumnInfo(name = "base_code")
    private String baseCurrencyCode;

    @ColumnInfo(name = "target_code")
    private String targetCurrencyCode;

    public DbModelEx() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseCurrencyName() {
        return baseCurrencyName;
    }

    public void setBaseCurrencyName(String baseCurrencyName) {
        this.baseCurrencyName = baseCurrencyName;
    }

    public String getBaseCurrencyValue() {
        return baseCurrencyValue;
    }

    public void setBaseCurrencyValue(String baseCurrencyValue) {
        this.baseCurrencyValue = baseCurrencyValue;
    }

    public String getTargetCurrencyName() {
        return targetCurrencyName;
    }

    public void setTargetCurrencyName(String targetCurrencyName) {
        this.targetCurrencyName = targetCurrencyName;
    }

    public String getTargetCurrencyValue() {
        return targetCurrencyValue;
    }

    public void setTargetCurrencyValue(String targetCurrencyValue) {
        this.targetCurrencyValue = targetCurrencyValue;
    }

    public String getTargetCurrencyCourse() {
        return targetCurrencyCourse;
    }

    public void setTargetCurrencyCourse(String targetCurrencyCourse) {
        this.targetCurrencyCourse = targetCurrencyCourse;
    }

    public String getDayTimeRequest() {
        return dayTimeRequest;
    }

    public void setDayTimeRequest(String dayTimeRequest) {
        this.dayTimeRequest = dayTimeRequest;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }
}
