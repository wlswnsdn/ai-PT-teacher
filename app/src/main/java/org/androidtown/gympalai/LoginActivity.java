package org.androidtown.gympalai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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

            }
        });



    }


}
