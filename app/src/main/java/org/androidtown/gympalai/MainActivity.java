package org.androidtown.gympalai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationBarView;

import org.androidtown.gympalai.database.GymPalDB;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB 생성
        GymPalDB db = GymPalDB.getInstance(this);


    }
}