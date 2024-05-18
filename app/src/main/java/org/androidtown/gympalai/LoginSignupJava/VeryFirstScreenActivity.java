package org.androidtown.gympalai.LoginSignupJava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.androidtown.gympalai.LoginSignupJava.LoginActivity;
import org.androidtown.gympalai.R;

public class VeryFirstScreenActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.very_first_screen);
//        버튼객체 생성
        Button login_button;
        Button signup_button;
        login_button=findViewById(R.id.first_login_btn);
        signup_button=findViewById(R.id.first_signup_btn);
//        버튼을 클릭하면 해당 화면으로 전환된다.
        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), FirstSignupActivity.class);
                startActivity(intent);
            }
        });



    }
}