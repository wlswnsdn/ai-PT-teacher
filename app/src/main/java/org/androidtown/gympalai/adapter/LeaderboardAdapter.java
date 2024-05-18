package org.androidtown.gympalai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.androidtown.gympalai.R;
import org.androidtown.gympalai.model.LeaderboardItem;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private List<LeaderboardItem> items;

    public LeaderboardAdapter(List<LeaderboardItem> items) {
        this.items = items;
    }

    public void setItems(List<LeaderboardItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardItem item = items.get(position);
        holder.rank.setText(String.valueOf(item.getRank()));
        holder.userId.setText(item.getUserId());
        holder.score.setText(String.valueOf(item.getScore()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView rank, userId, score;

        LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank);
            userId = itemView.findViewById(R.id.username);
            score = itemView.findViewById(R.id.score);
        }
    }
}
