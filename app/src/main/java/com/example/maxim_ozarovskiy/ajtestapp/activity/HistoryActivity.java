package com.example.maxim_ozarovskiy.ajtestapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.adapter.HistoryActivityAdapter;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.HistoryActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;
import com.example.maxim_ozarovskiy.ajtestapp.presenter.HistoryActivityPresenter;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class HistoryActivity extends AppCompatActivity implements HistoryActivityContract.View{

    private HistoryActivityPresenter presenter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private HistoryActivityAdapter historyAdapter;
    private ProgressBar progressBar;
    private TextView errorMsg;
    private Button refreshBtn;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        initUI();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        presenter = new HistoryActivityPresenter(this, this);
        showProgressBar();
        presenter.callDbRecords();

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.callDbRecords();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.main_activity:
                                startSimpleTickerActivity();
                                break;
                            case R.id.history_activity:

                                break;
                            case R.id.compete_activity:
                                startCompleteTickerActivity();
                                break;
                        }
                        return false;
                    }
                });
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

    private void initUI(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        refreshBtn = findViewById(R.id.refresh_btn_history_activity);
        errorMsg = findViewById(R.id.error_msg_history_activity);
        recyclerView = findViewById(R.id.history_activity_recycler_view);
        progressBar = findViewById(R.id.history_activity_progress_bar);
    }

    private void setNewRecyclerAdapter(List<DbModelEx> newList){
        historyAdapter = new HistoryActivityAdapter(this, newList);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.simple_ticker_history_activity_menu_btn) {
            startSimpleTickerActivity();
            return true;
        } else if (id == R.id.complete_ticker_history_activity_menu_btn) {
            startCompleteTickerActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startCompleteTickerActivity() {
        Intent intent = new Intent(this, CompleteTickerActivity.class);
        startActivity(intent);
        finish();
    }

    private void startSimpleTickerActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
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
