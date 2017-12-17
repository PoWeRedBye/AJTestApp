package com.example.maxim_ozarovskiy.ajtestapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class CurrencyTypes implements Parcelable {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("statuses")
    @Expose
    private List<String> statuses = new ArrayList<String>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public CurrencyTypes() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeStringList(this.statuses);
    }

    protected CurrencyTypes(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.statuses = in.createStringArrayList();
    }

    public static final Creator<CurrencyTypes> CREATOR = new Creator<CurrencyTypes>() {
        @Override
        public CurrencyTypes createFromParcel(Parcel source) {
            return new CurrencyTypes(source);
        }

        @Override
        public CurrencyTypes[] newArray(int size) {
            return new CurrencyTypes[size];
        }
    };
}