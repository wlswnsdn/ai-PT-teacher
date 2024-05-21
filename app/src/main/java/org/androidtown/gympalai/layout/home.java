package org.androidtown.gympalai.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.adapter.LeaderboardAdapter;
import org.androidtown.gympalai.model.CircularProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class home extends Fragment {

    CircularProgressView personalScore;  // 개인 점수 표시 뷰
    RecyclerView recyclerView;  // 리더보드 리사이클러뷰
    TextView firstPlace, secondPlace, thirdPlace;  // 1, 2, 3등 사용자 이름 텍스트뷰
    TextView firstPlaceScore, secondPlaceScore, thirdPlaceScore;  // 1, 2, 3등 사용자 점수 텍스트뷰

    private static String currentUser = "user6"; // 현재 사용자 아이디를 user6으로 설정, 이부분 가져와야함.

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 뷰 초기화
        personalScore = view.findViewById(R.id.score);
        recyclerView = view.findViewById(R.id.leaderboard);
        firstPlace = view.findViewById(R.id.userfirst);
        secondPlace = view.findViewById(R.id.usersecond);
        thirdPlace = view.findViewById(R.id.userthird);
        firstPlaceScore = view.findViewById(R.id.scorefirst);
        secondPlaceScore = view.findViewById(R.id.scoresecond);
        thirdPlaceScore = view.findViewById(R.id.scorethird);

        // 리사이클러뷰 레이아웃 매니저 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // 더미 데이터 생성 (이 부분을 DB에서 데이터를 가져오도록 수정해야 함)
        List<UserScore> userList = generateDummyData();


        // 점수순으로 정렬
        Collections.sort(userList, new Comparator<UserScore>() {
            @Override
            public int compare(UserScore u1, UserScore u2) {
                return Integer.compare(u2.getScore(), u1.getScore()); // 점수 내림차순으로 정렬
            }
        });

        // 리더보드 어댑터 설정
        LeaderboardAdapter adapter = new LeaderboardAdapter(userList, currentUser);
        recyclerView.setAdapter(adapter);

        // 상위 3명 사용자 이름 및 점수 설정
        if (userList.size() >= 3) {
            firstPlace.setText(userList.get(0).getUserId());
            secondPlace.setText(userList.get(1).getUserId());
            thirdPlace.setText(userList.get(2).getUserId());

            firstPlaceScore.setText(String.valueOf(userList.get(0).getScore()));
            secondPlaceScore.setText(String.valueOf(userList.get(1).getScore()));
            thirdPlaceScore.setText(String.valueOf(userList.get(2).getScore()));
        }

        // 개인 점수 설정
        for (UserScore user : userList) {
            if (user.getUserId().equals(currentUser)) {
                personalScore.setScore(user.getScore()); // 현재 사용자 점수 설정
                break;
            }
        }

        // 현재 사용자를 중앙에 위치시키는 메서드 호출
        scrollToCurrentUser(userList);

        return view;
    }

    // 현재 사용자를 중앙에 위치시키는 메서드
    private void scrollToCurrentUser(List<UserScore> userList) {
        int position = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId().equals(currentUser)) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPositionWithOffset(position, recyclerView.getHeight() / 2);
            }
        }
    }

    // 더미 데이터 생성 메서드 (이 부분을 DB에서 데이터를 가져오도록 수정해야 함)
    private List<UserScore> generateDummyData() {
        List<UserScore> userList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            userList.add(new UserScore("user" + i, (int) (Math.random() * 1000)));
        }
        return userList;
    }

    // UserScore 클래스 (테스트용)
    public static class UserScore {
        private String userId;
        private int score;

        public UserScore(String userId, int score) {
            this.userId = userId;
            this.score = score;
        }

        public String getUserId() {
            return userId;
        }

        public int getScore() {
            return score;
        }
    }
}
