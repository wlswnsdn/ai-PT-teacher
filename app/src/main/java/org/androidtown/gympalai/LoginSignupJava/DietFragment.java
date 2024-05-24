package org.androidtown.gympalai.LoginSignupJava;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.dao.ChatDao;
import org.androidtown.gympalai.database.GymPalDB;
import org.androidtown.gympalai.model.LoginId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DietFragment extends Fragment {
    Button diet_btn_1, diet_btn_2, diet_btn_3;
    TextView diet_text_view_1, diet_text_view_2, diet_text_view_3;
    GymPalDB db;
    LoginId loginId = new LoginId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_diet, container, false);

        // DB 생성
        db = GymPalDB.getInstance(getActivity());

        // 버튼 선언
        diet_btn_1 = rootView.findViewById(R.id.diet_btn_1);
        diet_btn_2 = rootView.findViewById(R.id.diet_btn_2);
        diet_btn_3 = rootView.findViewById(R.id.diet_btn_3);

        // TextView들 선언
        diet_text_view_1 = rootView.findViewById(R.id.food_text_view_1);
        diet_text_view_2 = rootView.findViewById(R.id.food_text_view_2);
        diet_text_view_3 = rootView.findViewById(R.id.food_text_view_3);

        try {
            String response = new chatAsyncTask(db.chatDao()).execute(loginId.getLoginId()).get();
            List<String> diet_array;

            if (response != null) {
                diet_array = getSplitedNamesFromResponse(response);
            } else {
                diet_array = Arrays.asList("Take a recommendation", "Take a recommendation", "Take a recommendation");
            }

            // TextView에 텍스트 설정
            setTextViewText(diet_text_view_1, diet_array.get(0));
            setTextViewText(diet_text_view_2, diet_array.get(1));
            setTextViewText(diet_text_view_3, diet_array.get(2));

            // 버튼과 텍스트뷰의 가시성 설정
            setVisibility(diet_btn_1, diet_text_view_1);
            setVisibility(diet_btn_2, diet_text_view_2);
            setVisibility(diet_btn_3, diet_text_view_3);

            return rootView;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void setVisibility(Button button, TextView textView) {
        if (TextUtils.isEmpty(textView.getText().toString().trim())) {
            button.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void setTextViewText(TextView textView, String text) {
        textView.setText(text);
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

    // 메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class chatAsyncTask extends AsyncTask<String, Void, String> {
        private ChatDao chatDao;

        public chatAsyncTask(ChatDao chatDao) {
            this.chatDao = chatDao;
        }

        @Override // 백그라운드 작업(메인스레드 X)
        protected String doInBackground(String... userIds) {
            return chatDao.getDietList(userIds[0]);
        }
    }
}
