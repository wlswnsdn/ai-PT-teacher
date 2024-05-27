package org.androidtown.gympalai.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.backmethod.LoginFunction;
import org.androidtown.gympalai.dao.UserDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.mypagefragments.AvatarSetting;
import org.androidtown.gympalai.mypagefragments.LanguageSetting;
import org.androidtown.gympalai.mypagefragments.Logout;
import org.androidtown.gympalai.mypagefragments.MyInformationChangeFragment;
import org.androidtown.gympalai.mypagefragments.NicknamePassword;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class myPage extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private MyInformationChangeFragment myinformationchangefragment;
    private NicknamePassword nicknamepasswordfragment;
    private AvatarSetting avatarsetting;
    private LanguageSetting languagesetting;
    private Logout logout;

    private Button my_information_btn, nickname_change_btn, avatar_btn, language_change_btn, logout_btn;
    private LinearLayout buttonContainer; // 버튼들을 포함하는 레이아웃

    private TextView nickname_text_view;
    private CircleImageView profileImage;

    GymPalDB db;

    LoginFunction loginFunction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 프래그먼트 초기화
        myinformationchangefragment = new MyInformationChangeFragment();
        nicknamepasswordfragment = new NicknamePassword();
        avatarsetting = new AvatarSetting();
        languagesetting = new LanguageSetting();
        logout = new Logout();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_page_layout, container, false);

        // db 받아오기
        db = GymPalDB.getInstance(getActivity());
        loginFunction = new LoginFunction();

        // 버튼들 findViewById로 생성
        my_information_btn = rootView.findViewById(R.id.information_btn);
        nickname_change_btn = rootView.findViewById(R.id.nickname_password_btn);
        avatar_btn = rootView.findViewById(R.id.avatar_btn);
        language_change_btn = rootView.findViewById(R.id.language_setting_btn);
        logout_btn = rootView.findViewById(R.id.logout_btn);
        buttonContainer = rootView.findViewById(R.id.button_container); // 버튼들을 포함하는 레이아웃 초기화

        // CircleImageView findViewById로 생성
        profileImage = rootView.findViewById(R.id.basic_circle_image);
        //textview불러오기
        nickname_text_view=rootView.findViewById(R.id.nickname_view);

        // 버튼 클릭 이벤트 설정
        my_information_btn.setOnClickListener(v -> replaceFragment(myinformationchangefragment));
        nickname_change_btn.setOnClickListener(v -> replaceFragment(nicknamepasswordfragment));
        avatar_btn.setOnClickListener(v -> replaceFragment(avatarsetting));
        language_change_btn.setOnClickListener(v -> replaceFragment(languagesetting));
        logout_btn.setOnClickListener(v -> replaceFragment(logout));

        //textview에 닉네임 불러와서 넣어주시면 됩니다.
        String nickname_from_db = null;
        try {
            nickname_from_db = String.valueOf(new GetUserNickTask(db.userDao()).execute(loginFunction.getMyId()).get());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setTextViewText(nickname_text_view, nickname_from_db);

        // CircleImageView 클릭 이벤트 설정
        profileImage.setOnClickListener(v -> openImageChooser());

        return rootView;
    }
    private void setTextViewText(TextView textView, String text) {
        textView.setText(text);
    }

    private void replaceFragment(Fragment fragment) {
        buttonContainer.setVisibility(View.GONE); // 버튼들을 숨깁니다.
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.setting_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openImageChooser() {
        // 이미지 선택 인텐트
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 이미지를 선택한 다음 결과를 처리
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData(); // 선택한 이미지의 URI를 가져온다.
            try {
                // 선택한 이미지의 비트맵을 가져온다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // CircleImageView에 이미지를 설정한다.
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class GetUserNickTask extends AsyncTask<String, Void, String> { //닉네임을 가져오기
        private UserDao userDao;
        public GetUserNickTask(UserDao userDao){this.userDao = userDao;}

        @Override
        protected String doInBackground(String... strings) {
            String nickName = userDao.getUserNickById(strings[0]);
            return nickName;
        }
    }
}
