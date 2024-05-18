package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "ranking", foreignKeys = @ForeignKey(entity = User.class,
parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE))
public class Ranking {
    public Ranking(@NonNull LocalDateTime date, @NonNull String userId, int score) {
        this.date = date;
        this.userId = userId;
        this.score = score;
    }

    //PK: RankingId
    //columns : date, userId, score

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long rankingId;

    @NonNull
    @ColumnInfo(name = "date")
    private LocalDateTime date;

    @NonNull
    private String userId;

    @NonNull
    @ColumnInfo(name = "Score")
    private int score;

    public long getRankingId() {
        return rankingId;
    }

    public void setRankingId(long rankingId) {
        this.rankingId = rankingId;
    }

    @NonNull
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(@NonNull LocalDateTime date) {
        this.date = date;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "rankingId=" + rankingId +
                ", date=" + date +
                ", userId='" + userId + '\'' +
                ", score=" + score +
                '}';
    }
}
