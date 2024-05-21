package org.androidtown.gympalai.model;

public class UserTotalScore {
    private String userId;
    private int totalScore;

    public UserTotalScore(String userId, int totalScore) {
        this.userId = userId;
        this.totalScore = totalScore;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return totalScore;
    }

    public void setScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
