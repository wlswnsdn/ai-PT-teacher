package org.androidtown.gympalai.mypagefragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.androidtown.gympalai.R;

import java.util.Locale;

public class LanguageSetting extends Fragment {
    Button kr_btn, en_btn, jp_btn;
    TextView current_language, what_language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_page_language, container, false);

        // 버튼들 선언
        kr_btn = rootView.findViewById(R.id.kr_btn);
        en_btn = rootView.findViewById(R.id.en_btn);
        jp_btn = rootView.findViewById(R.id.jp_btn);

        // 텍스트 뷰 생성
        current_language = rootView.findViewById(R.id.present_language);
        what_language = rootView.findViewById(R.id.text_view1);

        // 현재 언어 설정
        setCurrentLanguage();

        // 한국어 버튼 클릭 리스너 설정
        kr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ko"); // 언어 설정을 한국어로 변경
            }
        });

        // 영어 버튼 클릭 리스너 설정
        en_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en"); // 언어 설정을 영어로 변경
            }
        });

        // 일본어 버튼 클릭 리스너 설정
        jp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ja"); // 언어 설정을 일본어로 변경
            }
        });

        return rootView;
    }

    // 현재 언어를 텍스트 뷰에 설정하는 메서드
    private void setCurrentLanguage() {
        String language = Locale.getDefault().getLanguage();
        switch (language) {
            case "ko":
                current_language.setText("한국어");
                what_language.setText("현재 언어: ");
                kr_btn.setText("한국어");
                en_btn.setText("영어");
                jp_btn.setText("일본어");


                break;
            case "en":
                current_language.setText("English");
                what_language.setText("current_language: ");
                kr_btn.setText("Korean");
                en_btn.setText("English");
                jp_btn.setText("Japanese");

                break;
            case "ja":
                current_language.setText("日本語");
                what_language.setText("現在の言語: ");
                kr_btn.setText("韓国語");
                en_btn.setText("英語");
                jp_btn.setText("日本語");

                break;
            default:
                current_language.setText(language); // 기타 언어
                break;
        }
    }

    // 애플리케이션의 언어를 변경하는 메서드
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // 현재 언어를 업데이트
        setCurrentLanguage();

        // 프래그먼트 재시작하여 언어 변경을 반영
        restartFragment();
    }

    // 프래그먼트를 재시작하는 메서드
    private void restartFragment() {
        getFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }
}