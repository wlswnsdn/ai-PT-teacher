package org.androidtown.gympalai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.androidtown.gympalai.layout.home;

//로그인 페이지를 담당하는 코드입니다
public class LoginActivity extends AppCompatActivity {
    private EditText login_id, login_password;
    private Button login_button;
    public void onCreate(Bundle savedInstanace){
        super.onCreate(savedInstanace);
        setContentView(R.layout.login_screen);

        login_id=findViewById(R.id.id_place);
        login_password=findViewById(R.id.password_place);

        login_button=findViewById(R.id.login_button_in_login_page);
        login_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //여기에 login() 사용해주시면 됩니다.
                //login_id.getText().toString(), login_password.getText().toString()
                if(true){//로그인 성공했을때
                    Toast success= Toast.makeText(getApplicationContext(),"로그인에 성공했습니다.",Toast.LENGTH_SHORT);
                    success.show();
                    Intent intent = new Intent(getApplicationContext(), home.class);
                    startActivity(intent);
                }else{//로그인 실패했을때
                    Toast failure= Toast.makeText(getApplicationContext(),"로그인에 실패했습니다.",Toast.LENGTH_SHORT);

                }
            }
        });



    }


}
