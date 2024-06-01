package org.androidtown.gympalai.model;

public class LeaderboardItem {
    private int rank;
    private String userId;
    private int score;

    private byte[] profilePicture; //이미지를 담는 비트맵 변수

    public LeaderboardItem(int rank, String userId, int score, byte[] profilePicture) {

        this.rank = rank;
        this.userId = userId;
        this.score = score;
        this.profilePicture = profilePicture; //셍성자에 추가
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
