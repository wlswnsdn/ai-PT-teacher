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
import org.androidtown.gympalai.model.CircularProgressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class home extends Fragment {

    CircularProgressView personalScore;
    RecyclerView recyclerView;
    TextView firstPlace, secondPlace, thirdPlace;
    TextView firstPlaceScore, secondPlaceScore, thirdPlaceScore;
    private static String currentUser = "user6"; // 현재 사용자를 user6으로 설정

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        personalScore = view.findViewById(R.id.score);
        recyclerView = view.findViewById(R.id.leaderboard);
        firstPlace = view.findViewById(R.id.userfirst);
        secondPlace = view.findViewById(R.id.usersecond);
        thirdPlace = view.findViewById(R.id.userthird);
        firstPlaceScore = view.findViewById(R.id.scorefirst);
        secondPlaceScore = view.findViewById(R.id.scoresecond);
        thirdPlaceScore = view.findViewById(R.id.scorethird);

        // 리사이클러뷰 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<User> userList = generateDummyData(); // 더미 데이터 생성

        // 점수순으로 정렬
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return Integer.compare(u2.getScore(), u1.getScore()); // 점수 내림차순으로 정렬
            }
        });

        LeaderboardAdapter adapter = new LeaderboardAdapter(userList);
        recyclerView.setAdapter(adapter);

        // 더미 데이터에서 상위 3명 설정
        if (userList.size() >= 3) {
            firstPlace.setText(userList.get(0).getName());
            secondPlace.setText(userList.get(1).getName());
            thirdPlace.setText(userList.get(2).getName());

            firstPlaceScore.setText(String.valueOf(userList.get(0).getScore()));
            secondPlaceScore.setText(String.valueOf(userList.get(1).getScore()));
            thirdPlaceScore.setText(String.valueOf(userList.get(2).getScore()));
        }

        personalScore.setScore(700); // 예시 점수 설정

        scrollToCurrentUser(userList); // 현재 사용자를 중앙에 위치시키는 메서드 호출

        return view;
    }

    private void scrollToCurrentUser(List<User> userList) {
        int position = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equals(currentUser)) {
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

    private List<User> generateDummyData() {
        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            userList.add(new User("user" + i, (int) (Math.random() * 1000)));
        }
        return userList;
    }

    // User 클래스 테스트용
    static class User {
        private String name;
        private int score;

        public User(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }

    // LeaderboardAdapter 클래스
    static class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

        private List<User> userList;

        public LeaderboardAdapter(List<User> userList) {
            this.userList = userList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            User user = userList.get(position);
            holder.rank.setText(String.valueOf(position + 1)); // 랭킹 설정
            holder.username.setText(user.getName());
            holder.score.setText(String.valueOf(user.getScore()));


        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView rank, username, score;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                rank = itemView.findViewById(R.id.rank);
                username = itemView.findViewById(R.id.username);
                score = itemView.findViewById(R.id.score);
            }
        }
    }
}
