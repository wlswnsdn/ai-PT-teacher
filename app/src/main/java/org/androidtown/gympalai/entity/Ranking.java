package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.time.LocalDateTime;

// 외래키 설정
@Entity(tableName = "ranking",
        primaryKeys = {"userId", "date"},
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "userId",
                childColumns = "userId", onDelete = ForeignKey.CASCADE)
)
public class Ranking {
    @NonNull
    private LocalDateTime date;
    @NonNull
    private String userId;

    @NonNull
    @ColumnInfo(name = "score")
    private Integer score;

    @NonNull
    public LocalDateTime getDate() {return date;}

    public void setDate(@NonNull LocalDateTime date) {this.date = date;}

    @NonNull
    public String getUserId() {return userId;}

    public void setUserId(@NonNull String userId) {this.userId = userId;}

    @NonNull
    public Integer getScore() {return score;}

    public void setScore(@NonNull Integer score) {this.score = score;}

    @Override
    public String toString() {
        return "Ranking{" +
                "date=" + date +
                ", userId='" + userId + '\'' +
                ", score=" + score +
                '}';
    }
}

