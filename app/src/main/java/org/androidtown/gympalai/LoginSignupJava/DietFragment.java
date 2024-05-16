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
import org.androidtown.gympalai.LoginSignupJava.plan;

public class DietFragment extends Fragment {
    Button diet_btn_1,diet_btn_2,diet_btn_3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_diet, container, false);


    // 버튼 선언
        diet_btn_1 = rootView.findViewById(R.id.diet_btn_1);
        diet_btn_2 = rootView.findViewById(R.id.diet_btn_2);
        diet_btn_3 = rootView.findViewById(R.id.diet_btn_3);

        //db에서 식단이름 불러와서 배열에 넣으시면 돼요.
        String[] diet_array = {"Button 1", "Button 2", "", };

        // 버튼에 텍스트 세팅
        setButtonText(diet_btn_1, diet_array[0]);
        setButtonText(diet_btn_2, diet_array[1]);
        setButtonText(diet_btn_3, diet_array[2]);

        // 버튼에 텍스트가 있으면 보여주고 없으면 안보여주는 메서드
        setButtonVisibility(diet_btn_1);
        setButtonVisibility(diet_btn_2);
        setButtonVisibility(diet_btn_3);

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
