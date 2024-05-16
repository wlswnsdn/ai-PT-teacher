package org.androidtown.gympalai.LoginSignupJava;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

        //Drawable 리소스
        final Drawable purpleBackground = ContextCompat.getDrawable(this, R.drawable.round_square_diet_exercise_purple);
        final Drawable whiteBackground = ContextCompat.getDrawable(this, R.drawable.round_square_diet_exercise_white);



        if (exercise_btn != null) {
            exercise_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exercise_btn.setBackground(purpleBackground);
                    diet_btn.setBackground(whiteBackground);

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
                    exercise_btn.setBackground(whiteBackground);
                    diet_btn.setBackground(purpleBackground);
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