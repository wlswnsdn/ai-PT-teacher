package org.androidtown.gympalai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.entity.Ranking;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 사용자 점수 리스트를 저장할 변수
    private List<Ranking> userList;
    // 현재 사용자 아이디를 저장할 변수
    private String currentUser;
    //  사용자 뷰 타입을 나타내는 상수, 0=타인, 1=자신
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_CURRENT_USER = 1;

    // 사용자 점수 리스트와 현재 사용자 아이디를 초기화
    public LeaderboardAdapter(List<Ranking> userList, String currentUser) {
        this.userList = userList;
        this.currentUser = currentUser;
    }

    @Override
    public int getItemViewType(int position) {
        // 현재 사용자와 일치하는 경우 VIEW_TYPE_CURRENT_USER 반환
        if (userList.get(position).getUserId().equals(currentUser)) {
            return VIEW_TYPE_CURRENT_USER;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 사용자만 다른색 레이어 적용
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CURRENT_USER) {
            View view = inflater.inflate(R.layout.item_leaderboard_mine, parent, false);
            return new CurrentUserViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_leaderboard, parent, false);
            return new NormalViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 사용자 데이터를 가져옴
        Ranking user = userList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_CURRENT_USER) {
            ((CurrentUserViewHolder) holder).bind(user, position);
        } else {
            ((NormalViewHolder) holder).bind(user, position);
        }
    }

    @Override
    public int getItemCount() {
        // 사용자 리스트의 크기를 반환
        return userList.size();
    }

    // 일반 사용자를 위한 ViewHolder 클래스
    static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView rank, username, score; // 순위, 사용자 이름, 점수를 표시하는 TextView

        // 아이템 뷰에서 TextView를 초기화
        NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            username = itemView.findViewById(R.id.username);
            score = itemView.findViewById(R.id.score);
        }

        // 사용자 데이터를 ViewHolder에 바인딩하는 메서드
        void bind(Ranking user, int position) {
            rank.setText(String.valueOf(position + 1)); // 순위를 설정
            username.setText(user.getUserId()); // 사용자 이름을 설정
            score.setText(String.valueOf(user.getScore())); // 점수를 설정
        }
    }

    // 현재 사용자를 위한 ViewHolder 클래스
    static class CurrentUserViewHolder extends RecyclerView.ViewHolder {
        TextView rank, username, score; // 순위, 사용자 이름, 점수를 표시하는 TextView

        // 아이템 뷰에서 TextView를 초기화
        CurrentUserViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            username = itemView.findViewById(R.id.username);
            score = itemView.findViewById(R.id.score);
        }

        // 사용자 데이터를 ViewHolder에 바인딩하는 메서드
        void bind(Ranking user, int position) {
            rank.setText(String.valueOf(position + 1)); // 순위를 설정
            username.setText(user.getUserId()); // 사용자 이름을 설정
            score.setText(String.valueOf(user.getScore())); // 점수를 설정
        }
    }
}
