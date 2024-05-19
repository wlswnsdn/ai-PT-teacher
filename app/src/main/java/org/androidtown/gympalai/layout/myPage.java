package org.androidtown.gympalai.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.mypagefragments.AvatarSetting;
import org.androidtown.gympalai.mypagefragments.LanguageSetting;
import org.androidtown.gympalai.mypagefragments.Logout;
import org.androidtown.gympalai.mypagefragments.MyInformationChangeFragment;
import org.androidtown.gympalai.mypagefragments.NicknamePassword;

public class myPage extends Fragment {
    private MyInformationChangeFragment myinformationchangefragment;
    private NicknamePassword nicknamepasswordfragment;
    private AvatarSetting avatarsetting;
    private LanguageSetting languagesetting;
    private Logout logout;


    private Button my_information_btn, nickname_change_btn, avatar_btn, language_change_btn, logout_btn;
    private LinearLayout buttonContainer; // 버튼들을 포함하는 레이아웃

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 프래그먼트 초기화
        myinformationchangefragment = new MyInformationChangeFragment();
        nicknamepasswordfragment = new NicknamePassword();
        avatarsetting = new AvatarSetting();
        languagesetting = new LanguageSetting();
        logout=new Logout();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_page_layout, container, false);

        // 버튼들 findViewById로 생성
        my_information_btn = rootView.findViewById(R.id.information_btn);
        nickname_change_btn = rootView.findViewById(R.id.nickname_password_btn);
        avatar_btn = rootView.findViewById(R.id.avatar_btn);
        language_change_btn = rootView.findViewById(R.id.language_setting_btn);
        logout_btn = rootView.findViewById(R.id.logout_btn);
        buttonContainer = rootView.findViewById(R.id.button_container); // 버튼들을 포함하는 레이아웃 초기화

        // 버튼 클릭 이벤트 설정
        my_information_btn.setOnClickListener(v -> replaceFragment(myinformationchangefragment));
        nickname_change_btn.setOnClickListener(v -> replaceFragment(nicknamepasswordfragment));
        avatar_btn.setOnClickListener(v -> replaceFragment(avatarsetting));
        language_change_btn.setOnClickListener(v -> replaceFragment(languagesetting));
        logout_btn.setOnClickListener(v -> replaceFragment(logout));


        return rootView;
    }

    private void replaceFragment(Fragment fragment) {
        buttonContainer.setVisibility(View.GONE); // 버튼들을 숨깁니다.
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.setting_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
