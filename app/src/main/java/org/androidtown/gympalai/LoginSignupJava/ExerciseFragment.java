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
import org.androidtown.gympalai.backmethod.LoginFunction;
import org.androidtown.gympalai.dao.ChatDao;
import org.androidtown.gympalai.database.GymPalDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExerciseFragment extends Fragment {
    Button exercise_btn_1, exercise_btn_2, exercise_btn_3, exercise_btn_4, exercise_btn_5;
    TextView exercise_textview_1, exercise_textview_2, exercise_textview_3, exercise_textview_4, exercise_textview_5;

    GymPalDB db;

    LoginFunction loginFunction = new LoginFunction();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_exercise, container, false);

        // DB 생성
        db = GymPalDB.getInstance(getActivity());
        // 버튼 선언
        exercise_btn_1 = rootView.findViewById(R.id.exercise_btn_1);
        exercise_btn_2 = rootView.findViewById(R.id.exercise_btn_2);
        exercise_btn_3 = rootView.findViewById(R.id.exercise_btn_3);
        exercise_btn_4 = rootView.findViewById(R.id.exercise_btn_4);
        exercise_btn_5 = rootView.findViewById(R.id.exercise_btn_5);

        // 텍스트뷰 선언
        exercise_textview_1 = rootView.findViewById(R.id.exercise_text_view_1);
        exercise_textview_2 = rootView.findViewById(R.id.exercise_text_view_2);
        exercise_textview_3 = rootView.findViewById(R.id.exercise_text_view_3);
        exercise_textview_4 = rootView.findViewById(R.id.exercise_text_view_4);
        exercise_textview_5 = rootView.findViewById(R.id.exercise_text_view_5);

        try {

            String response = getRoutineFromGPTResponse();

            List<String> exercise_routine_array = new ArrayList<>();

            if (response != null) {
                exercise_routine_array = getSplitedNamesFromResponse(response);
            } else {
                exercise_routine_array = Arrays.asList("Take a recommendation", "Take a recommendation", "Take a recommendation", "Take a recommendation", "Take a recommendation");
            }

            // 텍스트뷰에 텍스트 설정
            setTextViewText(exercise_textview_1, exercise_routine_array.get(0));
            setTextViewText(exercise_textview_2, exercise_routine_array.get(1));
            setTextViewText(exercise_textview_3, exercise_routine_array.get(2));
            setTextViewText(exercise_textview_4, exercise_routine_array.get(3));
            setTextViewText(exercise_textview_5, exercise_routine_array.get(4));

            // 버튼과 텍스트뷰의 가시성 설정
            setVisibility(exercise_btn_1, exercise_textview_1);
            setVisibility(exercise_btn_2, exercise_textview_2);
            setVisibility(exercise_btn_3, exercise_textview_3);
            setVisibility(exercise_btn_4, exercise_textview_4);
            setVisibility(exercise_btn_5, exercise_textview_5);

            return rootView;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRoutineFromGPTResponse() throws ExecutionException, InterruptedException {
        return new chatAsyncTask(db.chatDao()).execute(loginFunction.getMyId()).get();
    }

    //textview에 texxt없으면 버튼과 text뷰 모두 안보이게 한다.
    private void setVisibility(Button button, TextView textView) {
        if (TextUtils.isEmpty(textView.getText().toString().trim())) {
            button.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }
    //textview에 텍스트를 설정한다.
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
            return chatDao.getExerciseList(userIds[0]);
        }
    }
}
