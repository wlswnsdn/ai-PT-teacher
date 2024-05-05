package org.androidtown.gympalai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstSignupActivity extends AppCompatActivity {
    //layout파일에 있는 nickname, id, password를 입력하는 edittext들과 다음 회원가입창으로 이동하는 버튼 가져왔어요.
    EditText edit_nickname;
    EditText edit_id;
    EditText edit_passwrd;
    Button next_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_signup_layout);

        //edit text들과 버튼을 연결해줍니다.
        edit_nickname=findViewById(R.id.nickname_place);
        edit_id=findViewById(R.id.id_place_in_signup);
        edit_passwrd=findViewById(R.id.password_place_in_signup);
        next_btn=findViewById(R.id.first_signup_btn);


        next_btn.setOnClickListener(new View.OnClickListener(){
            //nickname,id,password를 테이블에 넣으시면 될것 같아요
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edit_nickname.getText().toString())){//닉네임을 입력하지 않고 빈칸으로 남겨두었다면 toast메세지 출력
                    Toast.makeText(getApplicationContext(),"닉네임이 비어있습니다.",Toast.LENGTH_SHORT);

                } else if (TextUtils.isEmpty(edit_id.getText().toString())) {//Id를 입력하지 않고 빈칸으로 남겨두었다면 toast메세지 출력
                    Toast.makeText(getApplicationContext(),"아이디가 비어있습니다.",Toast.LENGTH_SHORT);

                }else if (TextUtils.isEmpty(edit_passwrd.getText().toString())) {//password를 입력하지 않고 빈칸으로 남겨두었다면 toast메세지 출력
                        Toast.makeText(getApplicationContext(),"패스워드가 비어있습니다.",Toast.LENGTH_SHORT);
                }else{//모두 채우면 next를 통해서 다음페이지로 이동
                    Intent intent=new Intent(getApplicationContext(), NextSignupActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
}
