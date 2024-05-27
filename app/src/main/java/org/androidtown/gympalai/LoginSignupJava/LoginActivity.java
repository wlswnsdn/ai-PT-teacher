package org.androidtown.gympalai.LoginSignupJava;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import org.androidtown.gympalai.backmethod.LoginFunction;

import java.util.List;
import java.util.concurrent.ExecutionException;

// 로그인 페이지를 담당하는 코드입니다
public class LoginActivity extends AppCompatActivity {
    private EditText login_id, login_password;
    private Button login_button;
    private Button signup_in_login;

    LoginFunction loginFunction;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.login_screen);

        // DB 생성
        GymPalDB db = GymPalDB.getInstance(this);
        // app inspection 구동을 위한 옵저버
        db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
            }
        });

        login_id = findViewById(R.id.id_place);
        login_password = findViewById(R.id.password_place);
        login_button = findViewById(R.id.login_button_in_login_page);
        signup_in_login = findViewById(R.id.sign_up_btn_in_login);

        // 키보드 내리기 위한 터치 리스너 설정
        findViewById(R.id.login_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // 여기에 login() 사용해주시면 됩니다.
                // login_id.getText().toString(), login_password.getText().toString()
                // if문 사용해서 로그인 성공시, home으로 넘어가도록 한다.
                String id = login_id.getText().toString();
                String pw = login_password.getText().toString();
                boolean loginResult = false;
                try {

                    String pwCheck = String.valueOf(new GetUserPwTask(db.userDao()).execute(id).get());
                    if(pwCheck.equals(pw)){loginResult = true;}
                } catch (ExecutionException e) {

                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (loginResult) {
                    loginFunction = new LoginFunction(id);
                    Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, basicLayout.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "잘못된 아이디 또는 비밀번호입니다..", Toast.LENGTH_SHORT).show();
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

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private static class GetUserPwTask extends AsyncTask<String, Void, String> {
        private UserDao userDao;

        public GetUserPwTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected String doInBackground(String... strings) {
            String pw = userDao.getUserPwById(strings[0]); // pw 받아오기
            return pw;
        }
    }
}
