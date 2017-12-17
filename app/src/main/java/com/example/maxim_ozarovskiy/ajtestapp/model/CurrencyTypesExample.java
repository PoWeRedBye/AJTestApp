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


public class CurrencyTypesExample implements Parcelable {

    @SerializedName("rows")
    @Expose
    private List<CurrencyTypes> currencyTypes = new ArrayList<CurrencyTypes>();

    public List<CurrencyTypes> getCurrencyTypes() {
        return currencyTypes;
    }

    public void setCurrencyTypes(List<CurrencyTypes> currencyTypes) {
        this.currencyTypes = currencyTypes;
    }

    public CurrencyTypesExample() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.currencyTypes);
    }

    protected CurrencyTypesExample(Parcel in) {
        this.currencyTypes = in.createTypedArrayList(CurrencyTypes.CREATOR);
    }

    public static final Creator<CurrencyTypesExample> CREATOR = new Creator<CurrencyTypesExample>() {
        @Override
        public CurrencyTypesExample createFromParcel(Parcel source) {
            return new CurrencyTypesExample(source);
        }

        @Override
        public CurrencyTypesExample[] newArray(int size) {
            return new CurrencyTypesExample[size];
        }
    };
}