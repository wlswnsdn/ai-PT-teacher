package org.androidtown.gympalai.LoginSignupJava;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.User;
import org.androidtown.gympalai.layout.basicLayout;
import org.androidtown.gympalai.layout.home;
import org.androidtown.gympalai.backmethod.LoginFunction;
import org.androidtown.gympalai.layout.plan;

import java.util.List;
import java.util.concurrent.ExecutionException;

//로그인 페이지를 담당하는 코드입니다
public class LoginActivity extends AppCompatActivity {
    private EditText login_id, login_password;
    private Button login_button;
    private Button signup_in_login;

    LoginFunction loginFunction;
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.login_screen);

        //DB 생성
        GymPalDB db = GymPalDB.getInstance(this);
        // app inspection 구동을 위한 옵저버
        db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
            }
        });

        login_id=findViewById(R.id.id_place);
        login_password=findViewById(R.id.password_place);
        login_button=findViewById(R.id.login_button_in_login_page);
        signup_in_login=findViewById(R.id.sign_up_btn_in_login);

        login_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //여기에 login() 사용해주시면 됩니다.
                //login_id.getText().toString(), login_password.getText().toString()
                //if문 사용해서 로그인 성공시, home으로 넘어가도록 한다.
                String id = login_id.getText().toString();
                String pw = login_password.getText().toString();
                boolean loginResult = false;
                try {
                    String pwCheck = String.valueOf(new GetUserPwTask(db.userDao()).execute(id).get());
                    if(pwCheck.equals(pw)){loginResult = true;}
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);}
                if(loginResult){
                    loginFunction = new LoginFunction(id);
                    Toast.makeText(getApplicationContext(),"로그인에 성공했습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, basicLayout.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"잘못된 아이디 또는 비밀번호입니다..",Toast.LENGTH_SHORT).show();

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





    private static class GetUserPwTask extends AsyncTask<String, Void, String>{
        private UserDao userDao;
        public GetUserPwTask(UserDao userDao){this.userDao = userDao;}

        @Override
        protected String doInBackground(String... strings) {
            String pw = userDao.getUserPwById(strings[0]); //pw받아오기
            return pw;
        }
    }
}

