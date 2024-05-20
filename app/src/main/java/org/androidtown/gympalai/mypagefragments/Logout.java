package org.androidtown.gympalai.mypagefragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.androidtown.gympalai.LoginSignupJava.LoginActivity;
import org.androidtown.gympalai.LoginSignupJava.NextSignupActivity;
import org.androidtown.gympalai.LoginSignupJava.VeryFirstScreenActivity;
import org.androidtown.gympalai.R;
import org.androidtown.gympalai.backmethod.LogOutFunction;

public class Logout extends Fragment {
    Button logout_btn;
    LogOutFunction logOutFunction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.my_page_layout, container, false);
        //버튼 선언
        logout_btn=rootView.findViewById(R.id.logout_btn);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//로그아웃이 되면 맨처음 페이지로 되돌아갑니다. if문 안에 구현해주시면 되겠습니다.
                if(true){//로그아웃을 처리하고 처음페이지로 되돌아간다.
                    logOutFunction = new LogOutFunction();
                    Toast.makeText(getContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), VeryFirstScreenActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });

        return rootView;
    }
}
