package com.codetek.lottaryapp.Views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codetek.lottaryapp.HistoryFragment;
import com.codetek.lottaryapp.R;
import com.codetek.lottaryapp.ScanFragment;
import com.codetek.lottaryapp.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment=new HomeFragment();
    ScanFragment scanFragment=new ScanFragment();
    HistoryFragment historyFragment=new HistoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_frame_layout, homeFragment ).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.dashboard_scanner:
                        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_frame_layout, homeFragment ).commit();
                        return true;
                    case R.id.dashboard_scan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_frame_layout, scanFragment ).commit();
                        return true;
                    case R.id.dashboard_history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_frame_layout, historyFragment ).commit();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}