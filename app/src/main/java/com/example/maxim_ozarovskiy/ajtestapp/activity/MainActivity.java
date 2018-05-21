package com.example.maxim_ozarovskiy.ajtestapp.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.fragments.CompleteFragment;
import com.example.maxim_ozarovskiy.ajtestapp.fragments.HistoryFragment;
import com.example.maxim_ozarovskiy.ajtestapp.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initUI();

        bottomNavigation.setSelectedItemId(R.id.main_activity);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.main_activity:
                        fragment = new MainFragment();
                        setTitle(R.string.simple_activity);
                        break;
                    case R.id.history_activity:
                        fragment = new HistoryFragment();
                        setTitle(R.string.converter_history);
                        break;
                    case R.id.complete_activity:
                        fragment = new CompleteFragment();
                        setTitle(R.string.complete_ticker_menu);
                        break;
                }
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

    }

    private void initUI() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        fragment = new MainFragment();
        transaction.replace(R.id.main_container,fragment).commit();
    }

}
