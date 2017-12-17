package com.example.maxim_ozarovskiy.ajtestapp.model;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class DbModelEx {

    private int id;
    private String baseCurrencyName;
    private String baseCurrencyValue;
    private String targetCurrencyName;
    private String targetCurrencyValue;
    private String targetCyrrencyCourse;
    private String dayTimeRequest;
    private String baseCurrencyCode;
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

    public String getTargetCyrrencyCourse() {
        return targetCyrrencyCourse;
    }

    public void setTargetCyrrencyCourse(String targetCyrrencyCourse) {
        this.targetCyrrencyCourse = targetCyrrencyCourse;
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
