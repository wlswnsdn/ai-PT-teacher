package org.androidtown.gympalai.LoginSignupJava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.androidtown.gympalai.R;

public class plan extends AppCompatActivity {
    ExerciseFragment exerciseFragment;
    DietFragment dietFragment;

    Button exercise_btn, diet_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_layout);

        //버튼 선언
        exercise_btn=findViewById(R.id.exercise_frag_btn);
        diet_btn=findViewById(R.id.diet_frag_btn);

        //fragment들 선언
        exerciseFragment = new ExerciseFragment();
        //최초에 fragment layout에 렌더링 되는 것은 exerciseFragment이다.
        getSupportFragmentManager().beginTransaction().replace(R.id.exercise_diet_frame, exerciseFragment).commit();
        dietFragment=new DietFragment();


        if (exercise_btn != null) {
            exercise_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exercise_btn.setBackgroundResource(R.color.basic_purple);
                    diet_btn.setBackgroundResource(R.color.white);

                    plan plan_activity= plan.this;
                    plan_activity.onFragmentChanged(0);
                }
            });
        } else {
            Log.e("DietFragment", "button_exercise is null");
        }

        if (diet_btn != null) {
            diet_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diet_btn.setBackgroundResource(R.color.basic_purple);
                    exercise_btn.setBackgroundResource(R.color.white);
                    plan plan_activity= plan.this;
                    plan_activity.onFragmentChanged(1);
                }
            });
        } else {
            Log.e("DietFragment", "button_diet is null");
        }

    }

    public void onFragmentChanged(int index){
        if(index==0){
            getSupportFragmentManager().beginTransaction().replace(R.id.exercise_diet_frame, exerciseFragment).commit();

        }
        else if(index==1){
            getSupportFragmentManager().beginTransaction().replace(R.id.exercise_diet_frame, dietFragment).commit();

        }
    }
}