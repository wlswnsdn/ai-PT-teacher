package org.androidtown.gympalai.LoginSignupJava;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.androidtown.gympalai.LoginSignupJava.NextSignupActivity;
import org.androidtown.gympalai.R;

public class FirstSignupActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_signup_layout);

        EditText edit_nickname = findViewById(R.id.nickname_place);
        EditText edit_id = findViewById(R.id.id_place_in_signup);
        EditText edit_passwrd = findViewById(R.id.password_place_in_signup);
        Button next_btn = findViewById(R.id.first_signup_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_nickname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "닉네임이 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_id.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "아이디가 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_passwrd.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "패스워드가 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent_2 = new Intent(FirstSignupActivity.this, NextSignupActivity.class);
                    startActivity(intent_2);
                }
            }
        });
    }
}
