package org.androidtown.gympalai.layout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.androidtown.gympalai.R;

public class plan extends Fragment {
    ExerciseFragment exerciseFragment;
    DietFragment dietFragment;

    Button exercise_btn, diet_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plan_layout, container, false);

        //버튼 선언
        exercise_btn = view.findViewById(R.id.exercise_frag_btn);
        diet_btn = view.findViewById(R.id.diet_frag_btn);

        //fragment들 선언
        exerciseFragment = new ExerciseFragment();
        //최초에 fragment layout에 렌더링 되는 것은 exerciseFragment이다.
        getChildFragmentManager().beginTransaction().replace(R.id.exercise_diet_frame, exerciseFragment).commit();
        dietFragment = new DietFragment();

        //Drawable 리소스
        final Drawable purpleBackground = ContextCompat.getDrawable(requireContext(), R.drawable.round_square_diet_exercise_purple);
        final Drawable whiteBackground = ContextCompat.getDrawable(requireContext(), R.drawable.round_square_diet_exercise_white);

        if (exercise_btn != null) {
            exercise_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exercise_btn.setBackground(purpleBackground);
                    diet_btn.setBackground(whiteBackground);

                    onFragmentChanged(0);
                }
            });
        } else {
            Log.e("PlanFragment", "button_exercise is null");
        }

        if (diet_btn != null) {
            diet_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exercise_btn.setBackground(whiteBackground);
                    diet_btn.setBackground(purpleBackground);

                    onFragmentChanged(1);
                }
            });
        } else {
            Log.e("PlanFragment", "button_diet is null");
        }

        return view;
    }

    public void onFragmentChanged(int index) {
        if (index == 0) {
            getChildFragmentManager().beginTransaction().replace(R.id.exercise_diet_frame, exerciseFragment).commit();
        } else if (index == 1) {
            getChildFragmentManager().beginTransaction().replace(R.id.exercise_diet_frame, dietFragment).commit();
        }
    }
}
