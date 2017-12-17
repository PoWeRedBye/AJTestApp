package com.example.maxim_ozarovskiy.ajtestapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class SimpleTickerExample {

    @SerializedName("ticker")
    @Expose
    private SimpleTicker simpleTicker;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private String error;

    public SimpleTicker getSimpleTicker() {
        return simpleTicker;
    }

    public void setSimpleTicker(SimpleTicker simpleTicker) {
        this.simpleTicker = simpleTicker;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
