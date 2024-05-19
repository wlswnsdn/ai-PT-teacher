package org.androidtown.gympalai.LoginSignupJava;

import android.content.Intent;
import android.os.AsyncTask;
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
import androidx.lifecycle.Observer;

import org.androidtown.gympalai.LoginSignupJava.LoginActivity;
import org.androidtown.gympalai.R;
import org.androidtown.gympalai.dao.HealthInfoDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.HealthInfo;
import org.androidtown.gympalai.entity.User;

import java.util.List;

public class NextSignupActivity extends AppCompatActivity {
    String[] purposes={"다이어트","벌크업","유지어트"};
    String[] exercise_num={"운동하지 않음","일주일에 1~2회","일주일에 3~5회","일주일에 6~7회","하루에 2회"};

    RadioGroup radGroupGender;
    RadioButton male_r_btn, female_r_btn;

    Button complete_signup;
    EditText Height_number;
    EditText Weight_number;
    EditText Age_number;

    int purpose;
    int exercise_num_index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_signup);

        //DB 생성
        GymPalDB db = GymPalDB.getInstance(this);
        // app inspection 구동을 위한 옵저버
        db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
            }
        });


        //spinner 생성
        Spinner spinner_purpose=findViewById(R.id.purpose_spinner);
        Spinner spinner_exercise_num=findViewById(R.id.exercise_num_spinner);
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

        //spinner 생성
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,purposes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_purpose.setAdapter(adapter);

        //spinner 생성
        ArrayAdapter<String> adapter_2= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,exercise_num);
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_exercise_num.setAdapter(adapter_2);
        
        
        //purpose spinner
        spinner_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String purpose_in_DB=purposes[position];
                purpose = position; // 운동목적의 data type이 int
                // 다이어트 벌크업 유지어트 순서대로 0, 1, 2임
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //exercise num spinner
        spinner_exercise_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercise_num_index = position; // 운동횟수의 data type이 int
                // 운동강도가 순서대로 0, 1, 2임
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
                    } else {
                        String userId = getIntent().getStringExtra("userId");
                        float height = Float.valueOf(Height_number.getText().toString());
                        float weight = Float.valueOf(Weight_number.getText().toString());
                        int age = Integer.parseInt(Age_number.getText().toString());
                        HealthInfo healthInfo = new HealthInfo(userId, height, weight, age, gender, 0, purpose); // Activity는 수정해주시면 입력기능 추가할게요
                        new InsertAsyncTask(db.healthInfoDao()).execute(healthInfo);

                        Intent intent = new Intent(NextSignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "모든 값을 올바르게 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static class InsertAsyncTask extends AsyncTask<HealthInfo, Void, Void> {
        private HealthInfoDao healthInfoDao;
        public InsertAsyncTask(HealthInfoDao healthInfoDao){
            this.healthInfoDao = healthInfoDao;
        }
        @Override
        protected Void doInBackground(HealthInfo... healthInfos) {
            healthInfoDao.insert(healthInfos[0]);
            return null;
        }
    }
}