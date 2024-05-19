package org.androidtown.gympalai.mypagefragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.androidtown.gympalai.R;

public class AvatarSetting extends Fragment {
    ImageView current_trainner, trainner_option_1,trainner_option_2,trainner_option_3;
    TextView current_trainner_name;
    Button trainner1, trainner2, trainner3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.my_page_avatar, container, false);

        //imageView들 선언
        current_trainner=rootView.findViewById(R.id.trainner_circle_1);
        trainner_option_1=rootView.findViewById(R.id.trainner_option_circle_1);
        trainner_option_2=rootView.findViewById(R.id.trainner_option_circle_2);
        trainner_option_3=rootView.findViewById(R.id.trainner_option_circle_3);

        //textview선언
        current_trainner_name=rootView.findViewById(R.id.trainer_name_textview);
        //Button들 선언
        trainner1=rootView.findViewById(R.id.trainner_btn_1);
        trainner2=rootView.findViewById(R.id.trainner_btn_2);
        trainner3=rootView.findViewById(R.id.trainner_btn_3);
        //버튼 클릭시 current_trainner의 src, current_trainner_name의 텍스트를 해당 아바타의 값들로 바꿉니다.
        trainner1.setOnClickListener(new View.OnClickListener() {//trainer1버튼을 누르면 avatar가 바뀐다.
            @Override
            public void onClick(View v) {//여기에 avatar1 구현해주시면됩니다.
                String sourceText = trainner1.getText().toString();
                current_trainner_name.setText(sourceText);
                current_trainner.setImageDrawable(trainner_option_1.getDrawable());
            }
        });

        trainner2.setOnClickListener(new View.OnClickListener() {//trainer2버튼을 누르면 avatar가 바뀐다.
            @Override
            public void onClick(View v) {
                String sourceText = trainner2.getText().toString();
                current_trainner_name.setText(sourceText);
                current_trainner.setImageDrawable(trainner_option_2.getDrawable());
            }
        });

        trainner3.setOnClickListener(new View.OnClickListener() {//trainer3버튼을 누르면 avatar가 바뀐다.
            @Override
            public void onClick(View v) {
                String sourceText = trainner3.getText().toString();
                current_trainner_name.setText(sourceText);
                current_trainner.setImageDrawable(trainner_option_3.getDrawable());
            }
        });


        return rootView;
    }
}
