package org.androidtown.gympalai.LoginSignupJava;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    EditText exercise_number;




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
        //edittext들 생성
        Height_number=findViewById(R.id.height_place);
        Weight_number=findViewById(R.id.weight_place);
        Weight_number=findViewById(R.id.weight_place);
        Age_number=findViewById(R.id.age_place);
        exercise_number=findViewById(R.id.weekly_ex_num);

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


        //최종 signup 버튼이 눌렸을 때 //edittext에 값이 안들어가있을때, toast메세지를 띄웁니다.
        complete_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int radioId = radGroupGender.getCheckedRadioButtonId();
                    boolean gender;
                    if (male_r_btn.getId() == radioId) {
                        gender = true;
                    } else if (female_r_btn.getId() == radioId) {
                        gender = false;
                    } else {
                        Toast.makeText(getApplicationContext(), "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(Height_number.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "키를 입력해주세요(cm)", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(Weight_number.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "몸무게를 입력해주세요(kg)", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(Age_number.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "나이를 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(exercise_number.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "일주일에 얼마나 많이 운동하시는지 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {//여기에서 height랑 weight랑 age랑 exercise number db에 넣으시면 될것 같아요
                        int height = Integer.parseInt(Height_number.getText().toString());
                        int weight = Integer.parseInt(Weight_number.getText().toString());
                        int age = Integer.parseInt(Age_number.getText().toString());
                        int exercise = Integer.parseInt(exercise_number.getText().toString());
                        String purpose_in_DB = (String) spinner.getSelectedItem();
                        Intent intent = new Intent(NextSignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "모든 값을 올바르게 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}