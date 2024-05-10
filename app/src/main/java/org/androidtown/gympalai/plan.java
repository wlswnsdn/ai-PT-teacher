package org.androidtown.gympalai;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class plan extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_layout);
//        운동부분 버튼 5개가 들어있는 relative layout과 버튼들 선언
        RelativeLayout exercise_relativeLayout = findViewById(R.id.exercise_relativelayout);
        Button exercise_button1 = findViewById(R.id.exercise_btn_1);
        Button exercise_button2 = findViewById(R.id.exercise_btn_2);
        Button exercise_button3 = findViewById(R.id.exercise_btn_3);
        Button exercise_button4 = findViewById(R.id.exercise_btn_4);
        Button exercise_button5 = findViewById(R.id.exercise_btn_5);

        //        식단부분 버튼 5개가 들어있는 relative layout과 버튼들 선언
        RelativeLayout diet_relativeLayout = findViewById(R.id.diet_relative_layout);
        Button diet_button1 = findViewById(R.id.diet_btn_1);
        Button diet_button2 = findViewById(R.id.diet_btn_2);
        Button diet_button3 = findViewById(R.id.diet_btn_3);

        //운동과 식단을 저장하는 리스트를 선언합니다. 여기에서 getRoutineFromGPTResponse()랑 getDietFromGPTResponse()로 리스트에 운동이랑 식단 넣어주시면 됩니다.
        List<String> exercise_buttonLabels = new ArrayList<>();
        List<String> diet_buttonLabels = new ArrayList<>();



        if (exercise_buttonLabels.size() >= 5) { // 운동리스트에 5개 모두 들어갔을 때 버튼 텍스트를 setting한다.
            exercise_button1.setText(exercise_buttonLabels.get(0));
            exercise_button2.setText(exercise_buttonLabels.get(1));
            exercise_button3.setText(exercise_buttonLabels.get(2));
            exercise_button4.setText(exercise_buttonLabels.get(3));
            exercise_button5.setText(exercise_buttonLabels.get(4));
        }

        if (diet_buttonLabels.size() >= 3) { // 식단리스트에 3개 모두 들어갔을 때 버튼 텍스트를 setting한다.
            diet_button1.setText(diet_buttonLabels.get(0));
            diet_button2.setText(diet_buttonLabels.get(1));
            diet_button3.setText(diet_buttonLabels.get(2));
        }


        // 버튼에 텍스트가 들어있다면 relative layout을 보이게 설정하고 버튼에 텍스트가 들어있지 않다면 안보이게 설정
        if (isAnyButtonTextEmpty(exercise_button1, exercise_button2, exercise_button3, exercise_button4, exercise_button5)) {
            exercise_relativeLayout.setVisibility(View.GONE);
        } else {
            exercise_relativeLayout.setVisibility(View.VISIBLE);
        }
        if (isAnyButtonTextEmpty(diet_button1, diet_button2, diet_button3)) {
            diet_relativeLayout.setVisibility(View.GONE);
        } else {
            diet_relativeLayout.setVisibility(View.VISIBLE);
        }

    }

//    버튼에 텍스트가 들어갔는지 안들어갔는지 확인하는 함수, 텍스트가 없을때 true, 있을 때 false를 반환한다.
    private boolean isAnyButtonTextEmpty(Button... buttons) {
        for (Button button : buttons) {
            if (button.getText().toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}