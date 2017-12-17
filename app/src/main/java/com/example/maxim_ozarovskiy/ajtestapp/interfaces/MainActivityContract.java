package com.example.maxim_ozarovskiy.ajtestapp.interfaces;


/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public interface MainActivityContract {

    interface View {
        void callbackErrorMessage(String error);
        void callbackConversion(String targetValue);
    }

    interface Presenter {
        void callConversion(String base, String target, String convertationValue);
    }
}
