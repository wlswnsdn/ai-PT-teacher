package org.androidtown.gympalai.model;

public class LeaderboardItem {
    private int rank;
    private String userId;
    private int score;

    public LeaderboardItem(int rank, String userId, int score) {
        this.rank = rank;
        this.userId = userId;
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public String getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }
}
