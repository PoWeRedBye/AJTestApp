package com.example.maxim_ozarovskiy.ajtestapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.adapter.HistoryActivityAdapter;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.HistoryContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;
import com.example.maxim_ozarovskiy.ajtestapp.presenter.HistoryPresenter;

import java.util.List;

public class HistoryFragment extends Fragment implements HistoryContract.View {

    private HistoryPresenter presenter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private HistoryActivityAdapter historyAdapter;
    private ProgressBar progressBar;
    private TextView errorMsg;
    private Button refreshBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_fragment, container, false);
        initUI(v);
        presenter = new HistoryPresenter(getActivity(),this);
        showProgressBar();
        presenter.callDbRecords();
        refreshBtn.setOnClickListener(this::refreshBtnClick);

        return v;
    }

    private void refreshBtnClick(View v){
        presenter.callDbRecords();
    }

    private void initUI(View v){
        refreshBtn = v.findViewById(R.id.refresh_btn_history_activity);
        errorMsg = v.findViewById(R.id.error_msg_history_activity);
        recyclerView = v.findViewById(R.id.history_activity_recycler_view);
        progressBar = v.findViewById(R.id.history_activity_progress_bar);
    }

    private void setNewRecyclerAdapter(List<DbModelEx> newList){
        historyAdapter = new HistoryActivityAdapter(getActivity(), newList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.animate();
        recyclerView.setVisibility(View.INVISIBLE);
        refreshBtn.setVisibility(View.INVISIBLE);
        errorMsg.setVisibility(View.INVISIBLE);
    }

    private void showRefreshBtn(){
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.clearAnimation();
        errorMsg.setVisibility(View.VISIBLE);
        refreshBtn.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView(){
        progressBar.clearAnimation();
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        refreshBtn.setVisibility(View.INVISIBLE);
        errorMsg.setVisibility(View.INVISIBLE);
    }

    @Override
    public void callbackDbRecords(List<DbModelEx> dbList) {
        showRecyclerView();
        setNewRecyclerAdapter(dbList);
    }

    @Override
    public void callbackErrorMsg(String error) {
        errorMsg.setText(error);
        showRefreshBtn();
    }
}
