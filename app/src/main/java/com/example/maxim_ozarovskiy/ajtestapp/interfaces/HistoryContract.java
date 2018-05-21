package com.example.maxim_ozarovskiy.ajtestapp.interfaces;

import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public interface HistoryContract {

    interface View {
        void callbackDbRecords(List<DbModelEx> dbList);
        void callbackErrorMsg(String error);
    }

    interface Presenter {
        void callDbRecords();
        void clearDisp();
    }
}
