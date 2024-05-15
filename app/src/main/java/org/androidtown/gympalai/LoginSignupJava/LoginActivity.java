package org.androidtown.gympalai.LoginSignupJava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.layout.home;

//로그인 페이지를 담당하는 코드입니다
public class LoginActivity extends AppCompatActivity {
    private EditText login_id, login_password;
    private Button login_button;
    private Button signup_in_login;
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.login_screen);

        login_id=findViewById(R.id.id_place);
        login_password=findViewById(R.id.password_place);
        login_button=findViewById(R.id.login_button_in_login_page);
        signup_in_login=findViewById(R.id.sign_up_btn_in_login);



        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //여기에 login() 사용해주시면 됩니다.
                //login_id.getText().toString(), login_password.getText().toString()
                if (true) {//로그인 성공했을때
                    Toast success = Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT);
                    success.show();
                    Intent intent = new Intent(getApplicationContext(), home.class);

//                    //if문 사용해서 로그인 성공시, home으로 넘어가도록 한다.
//                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, home.class);
//                    startActivity(intent);

                }
            }
        });




        signup_in_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FirstSignupActivity.class);
                startActivity(intent);
            }
        });
    }
}

