package org.androidtown.gympalai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.User;

import java.util.List;

public class InsertPractice extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.for_database_test);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        enter = findViewById(R.id.enter);

        //DB 생성
        GymPalDB db = GymPalDB.getInstance(this);

        // 값 들어갈 때마다 user table 조회 새로고침
        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
        db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                textView.setText(users.toString());
            }
        });

        //DB 데이터 불러오기 (SELECT)
        textView.setText(db.userDao().getAll().toString());

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim().length() <= 0) {
                    Toast.makeText(InsertPractice.this, "한글자 이상입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    // execute() 매개변수로 doInBackGround()에 들어가야되는 매개변수 넣으면 됨
                    new InsertAsyncTask(db.userDao()).execute(new
                            User(editText.getText().toString(), "1234","Jan","John"));
                    editText.setText("");
                }
            }
        });



    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class InsertAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public  InsertAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(User... users) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            try {
                if (users != null && users.length > 0) {
                    userDao.insert(users[0]);
                }
            } catch (Exception e) {
                System.out.println("AsyncTask Exception occurred: " + e.getMessage());
            }
            return null;

        }
    }
}