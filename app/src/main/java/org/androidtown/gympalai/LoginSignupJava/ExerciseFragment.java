package org.androidtown.gympalai.LoginSignupJava;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.androidtown.gympalai.R;

public class ExerciseFragment extends Fragment {
    Button exercise_btn_1,exercise_btn_2,exercise_btn_3,exercise_btn_4,exercise_btn_5;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_exercise, container, false);

        exercise_btn_1 = rootView.findViewById(R.id.exercise_btn_1);
        exercise_btn_2 = rootView.findViewById(R.id.exercise_btn_2);
        exercise_btn_3 = rootView.findViewById(R.id.exercise_btn_3);
        exercise_btn_4 = rootView.findViewById(R.id.exercise_btn_4);
        exercise_btn_5 = rootView.findViewById(R.id.exercise_btn_5);

        //db에서 운동이름 불러와서 배열에 넣으시면 돼요.
        String[] exercise_routine_array = {"Button 1", "Button 2", "", "Button 4", "Button 5"};

        // 버튼에 텍스트 세팅
        setButtonText(exercise_btn_1, exercise_routine_array[0]);
        setButtonText(exercise_btn_2, exercise_routine_array[1]);
        setButtonText(exercise_btn_3, exercise_routine_array[2]);
        setButtonText(exercise_btn_4, exercise_routine_array[3]);
        setButtonText(exercise_btn_5, exercise_routine_array[4]);

        // 버튼에 텍스트가 있으면 보여주고 없으면 안보여주는 메서드
        setButtonVisibility(exercise_btn_1);
        setButtonVisibility(exercise_btn_2);
        setButtonVisibility(exercise_btn_3);
        setButtonVisibility(exercise_btn_4);
        setButtonVisibility(exercise_btn_5);

        return rootView;
    }

    private void setButtonVisibility(Button button) {
        if (TextUtils.isEmpty(button.getText().toString().trim())) {
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
        }
    }

    private void setButtonText(Button button, String text) {
        button.setText(text);
    }
}
