package org.androidtown.gympalai.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.mypagefragments.AvatarSetting;
import org.androidtown.gympalai.mypagefragments.LanguageSetting;
import org.androidtown.gympalai.mypagefragments.MyInformationChangeFragment;
import org.androidtown.gympalai.mypagefragments.NicknamePassword;

//my를 담당하는 코드입니다.
public class myPage extends Fragment {
    MyInformationChangeFragment myinformationchangefragment;
    NicknamePassword nicknamepasswordfragment;
    AvatarSetting avatarsetting;
    LanguageSetting languagesetting;

    Button my_information_btn, nickname_change_btn, avatar_btn, language_change_btn, information_submit_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 프래그먼트 초기화
        myinformationchangefragment = new MyInformationChangeFragment();
        nicknamepasswordfragment = new NicknamePassword();
        avatarsetting = new AvatarSetting();
        languagesetting = new LanguageSetting();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_page_layout, container, false);

        //바뀐 정보를 갱신하는 버튼
        information_submit_btn=rootView.findViewById(R.id.information_submit_btn);
        // 버튼들 .findviewbyid로 생성
        my_information_btn = rootView.findViewById(R.id.information_btn);
        nickname_change_btn = rootView.findViewById(R.id.nickname_password_btn);
        avatar_btn = rootView.findViewById(R.id.avatar_btn);
        language_change_btn = rootView.findViewById(R.id.language_setting_btn);

        // 버튼 클릭 이벤트 설정
        my_information_btn.setOnClickListener(v -> replaceFragment(myinformationchangefragment));
        nickname_change_btn.setOnClickListener(v -> replaceFragment(nicknamepasswordfragment));
        avatar_btn.setOnClickListener(v -> replaceFragment(avatarsetting));
        language_change_btn.setOnClickListener(v -> replaceFragment(languagesetting));

        //바뀐 정보들을 저장하고 마지막으로 submit버튼을 누르면 유저의 건강정보가 갱신된다.
//        information_submit_btn.setOnClickListener(View.OnClickListener(
//
//        ));

        return rootView;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.setting_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
