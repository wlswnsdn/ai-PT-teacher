package org.androidtown.gympalai.LoginSignupJava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.androidtown.gympalai.R;

public class NextSignupActivity extends AppCompatActivity {
    String[] purposes={"다이어트","벌크업","유지어트"};

    RadioGroup radGroupGender;
    RadioButton male_r_btn, female_r_btn;

    Button complete_signup;
    EditText Height_number;
    EditText Weight_number;
    EditText Age_number;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_signup);

        //spinner 생성
        Spinner spinner=findViewById(R.id.purpose_spinner);
        //radijo group생성
        radGroupGender=findViewById(R.id.radioGroup);
        //radio button 생성
        male_r_btn=findViewById(R.id.male_r_btn);
        female_r_btn=findViewById(R.id.female_r_btn);
        //회원가입 완료 버튼
        complete_signup=findViewById(R.id.complete_signup_btn);


        //선택된 radio button에 따라서 boolean값으로 성별을 넣는다.
        int radioId=radGroupGender.getCheckedRadioButtonId();
        Boolean gender;
        if(male_r_btn.getId()==radioId){
            gender=true;
        }
        if(female_r_btn.getId()==radioId){
            gender=false;

        }
        //spinner 생성
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,purposes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //여기에 register()로 purpose_in_DB string을 DB에 넣어주시면 될 것 같아요
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //선택된 운동 목적을 String타입 변수 purpose_in_DB에 저장합니다.
                String purpose_in_DB=purposes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //최종 signup 버튼이 눌렸을 때
        complete_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true){//회원가입이 정상적으로 완료되었을 때, Login페이지로 돌아가서 로그인한다. 찬우님 여기에 회원가입 성공, 실패 boolean값으로 리턴되는 함수 넣어주세요
                    Toast.makeText(getApplicationContext(),"회원가입 완료",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(NextSignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"다시 시도해주세요",Toast.LENGTH_SHORT).show();

                }


            }
        });



    }
}
