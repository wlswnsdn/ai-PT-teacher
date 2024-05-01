package org.androidtown.gympalai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class basicLayout extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_layout);

        bottomNavigationView = findViewById(R.id.menubar);
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
                        selectedFragment = new Routine();
                        break;
                }
                if(selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, selectedFragment)
                            .commit();
                }
                return true;
            }
        });




    }
}