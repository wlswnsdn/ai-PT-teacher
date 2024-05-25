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

import org.androidtown.gympalai.LoginSignupJava.NextSignupActivity;
import org.androidtown.gympalai.R;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirstSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_signup_layout);

        // DB 생성
        GymPalDB db = GymPalDB.getInstance(this);
        // app inspection 구동을 위한 옵저버
        db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
            }
        });

        EditText edit_nickname = findViewById(R.id.nickname_place);
        EditText edit_id = findViewById(R.id.id_place_in_signup);
        EditText edit_passwrd = findViewById(R.id.password_place_in_signup);
        Button next_btn = findViewById(R.id.first_signup_btn);

        // 키보드 내리기 위한 터치 리스너 설정
        findViewById(R.id.first_signup_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_nickname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "닉네임이 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_id.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "아이디가 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edit_passwrd.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "패스워드가 비어있습니다.", Toast.LENGTH_SHORT).show();
                } else { // 닉네임, 아이디, Pw를 전부 입력했을 경우
                    // 동일한 아이디 또는 닉네임이 존재하는지 확인해야한다.
                    String id = edit_id.getText().toString();
                    String nickName = edit_nickname.getText().toString();
                    Boolean isSameId;
                    Boolean isSameNickName;
                    try {
                        isSameId = new CheckIdAsyncTask(db.userDao()).execute(id).get();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        isSameNickName = new CheckNickAsyncTask(db.userDao()).execute(nickName).get();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (isSameId) { // 동일한 아이디가 존재한다면 다른 아이디를 입력하도록 해야한다
                        Toast.makeText(getApplicationContext(), "동일한 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                    } else if (isSameNickName) { // 동일한 닉네임이 존재한다면 다른 닉네임을 입력하도록 한다
                        Toast.makeText(getApplicationContext(), "동일한 닉네임이 존재합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        String pw = edit_passwrd.getText().toString();

                        User user = new User(id, pw, nickName, "성민수"); //회원가입 할 user객체 생성
                        // DB조작은 Async로
                        //db.userDao().insert(user); //이렇게 코드를 짜고 싶은데 메인스레드에서는 작업할 수 없다

                        new InsertAsyncTask(db.userDao()).execute(user);

                        // 화면 전환
                        Intent intent_2 = new Intent(FirstSignupActivity.this, NextSignupActivity.class);
                        intent_2.putExtra("userId", id); // 2번째 화면에서 id 값이 필요함
                        startActivity(intent_2);
                    }
                }
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

    // DB 조작
    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> { // User 객체 넘겨받아서 Insert 작업을 수행할 거다
        // 비동기 처리에서 수행되는 내용이 userDao에 있는 Insert 함수이다
        private UserDao userDao; // User를 조작하기 위한 함수가 있는 UserDao 객체 생성
        public InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) { //여기서 비동기 처리 수행
            try {
                userDao.insert(users[0]);
            } catch (Exception e) {
                System.out.println("e.getMessage() = " + e.getMessage());
            }

            return null;
        }
    }
    private static class CheckIdAsyncTask extends AsyncTask<String, Void, Boolean> {
        // String id를 넘겨받아 Select 작업을 수행할 것이다
        private UserDao userDao;
        public CheckIdAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean result = userDao.getUserIdList().contains(strings[0]);
            return result;
        }
    }
    private static class CheckNickAsyncTask extends AsyncTask<String, Void, Boolean> {
        private UserDao userDao;
        public CheckNickAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean result = userDao.getUserNickList().contains(strings[0]);
            return result;
        }
    }
}
