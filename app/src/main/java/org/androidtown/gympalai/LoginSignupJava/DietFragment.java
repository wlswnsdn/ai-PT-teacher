package org.androidtown.gympalai.LoginSignupJava;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.layout.plan;
import org.androidtown.gympalai.dao.ChatDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.entity.Chat;
import org.androidtown.gympalai.model.LoginId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DietFragment extends Fragment {
    Button diet_btn_1,diet_btn_2,diet_btn_3;

    GymPalDB db;

    LoginId loginId=new LoginId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.fragment_diet, container, false);

        //DB 생성
        db = GymPalDB.getInstance(getActivity());

        // 버튼 선언
        diet_btn_1 = rootView.findViewById(R.id.diet_btn_1);
        diet_btn_2 = rootView.findViewById(R.id.diet_btn_2);
        diet_btn_3 = rootView.findViewById(R.id.diet_btn_3);


        try {
            String response = new chatAsyncTask(db.chatDao()).execute(loginId.getLoginId()).get();
            List<String> diet_array = new ArrayList<>();

            if (response != null) {
                List<String> dietList = getSplitedNamesFromResponse(response);
                for (String diet : dietList) {
                    diet_array.add(diet);
                }
            } else
                diet_array = Arrays.asList("Take a recommendation", "Take a recommendation", "Take a recommendation");

            // 버튼에 텍스트 세팅
            setButtonText(diet_btn_1, diet_array.get(0));
            setButtonText(diet_btn_2, diet_array.get(1));
            setButtonText(diet_btn_3, diet_array.get(2));

            // 버튼에 텍스트가 있으면 보여주고 없으면 안보여주는 메서드
            setButtonVisibility(diet_btn_1);
            setButtonVisibility(diet_btn_2);
            setButtonVisibility(diet_btn_3);

            return rootView;

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



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

    private List<String> getSplitedNamesFromResponse(String response) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(response);
        List<String> infoNames = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group(1);
            String[] names = group.split(",");
            for (String name : names) {
                infoNames.add(name.trim());
            }
        }
        return infoNames;
    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class chatAsyncTask extends AsyncTask<String, Void, String> {
        private ChatDao chatDao;

        public  chatAsyncTask(ChatDao chatDao){
            this.chatDao = chatDao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected String doInBackground(String ... userIds) {
            String dietList = chatDao.getDietList(userIds[0]);
            return dietList;

        }
    }
}
