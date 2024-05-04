package org.androidtown.gympalai;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NextSignupActivity extends AppCompatActivity {
    String[] purposes={"다이어트","벌크업","유지어트"};
    Spinner spinner=findViewById(R.id.purpose_spinner);
    RadioGroup radGroupGender;
    RadioButton male_r_btn, female_r_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_signup);

        //radijo group생성
        radGroupGender=findViewById(R.id.radioGroup);
        //radio button 생성
        male_r_btn=findViewById(R.id.male_r_btn);
        female_r_btn=findViewById(R.id.female_r_btn);
        //선택된 radio button에 따라서 String값으로 성별을 넣는다.
        int radioId=radGroupGender.getCheckedRadioButtonId();
        String gender;
        if(male_r_btn.getId()==radioId){
            gender="Male";
        }
        if(female_r_btn.getId()==radioId){
            gender="Female";

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



    }
}
