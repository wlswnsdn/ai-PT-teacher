package org.androidtown.gympalai.layout;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import org.androidtown.gympalai.R;

import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.User;
import org.androidtown.gympalai.worker.SeasonUpdateWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class basicLayout extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_layout);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        homeButton = findViewById(R.id.homebutton);


        //DB 생성
        GymPalDB db = GymPalDB.getInstance(this);

        // app inspection 구동을 위한 옵저버
        db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

            }
        });

        // 2주마다 ranking 초기화
        seasonUpdate();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.analysis:
                        selectedFragment = new analysis();
                        break;
                    case R.id.chat:
                        selectedFragment = new chat();
                        break;
                    case R.id.my:
                        selectedFragment = new myPage();
                        break;
                    case R.id.routine:
                        selectedFragment = new plan();
                        break;
                }
                if(selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.framelayout, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment homeFragment = new home();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout, homeFragment)
                        .commit();
            }
        });

        // Set homeFragment as the default fragment when the app starts
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framelayout, new home())
                    .commit();
        }
    }

    private void seasonUpdate() {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(SeasonUpdateWorker.class, 14, TimeUnit.DAYS).build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("SeasonUpdateWork", ExistingPeriodicWorkPolicy.KEEP, request);
    }
}
