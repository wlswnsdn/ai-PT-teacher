package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

// 외래키 설정
@Entity(tableName = "healthInfo",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "userId",
                childColumns = "userId", onDelete = ForeignKey.CASCADE)
)
public class HealthInfo {

    @NonNull
    @PrimaryKey
    private String userId;

    @ColumnInfo(name = "date")
    private LocalDateTime date;
    @NonNull
    @ColumnInfo(name = "height")
    private Float height;

    @NonNull
    @ColumnInfo(name = "weight")
    private Float weight;

    @NonNull
    @ColumnInfo(name = "age")
    private Integer age;

    //True -> 남자 / False -> 여자
    @NonNull
    @ColumnInfo(name = "gender")
    private Boolean gender;

    // 0~4 운동량에 따라 증가
    @NonNull
    @ColumnInfo(name = "activity")
    private Integer activity;

    //0 -> 다이어트 / 1-> 유지어터 / 2-> 벌크업
    @NonNull
    @ColumnInfo(name = "purpose")
    private Integer purpose;

    @NonNull
    public String getUserId() {return userId;}

    public void setUserId(@NonNull String userId) {this.userId = userId;}

    public LocalDateTime getDate() {return date;}

    public void setDate(LocalDateTime date) {this.date = date;}

    @NonNull
    public Float getHeight() {return height;}

    public void setHeight(@NonNull Float height) {this.height = height;}

    @NonNull
    public Float getWeight() {return weight;}

    public void setWeight(@NonNull Float weight) {this.weight = weight;}

    @NonNull
    public Integer getAge() {return age;}

    public void setAge(@NonNull Integer age) {this.age = age;}

    @NonNull
    public Boolean getGender() {return gender;}

    public void setGender(@NonNull Boolean gender) {this.gender = gender;}

    @NonNull
    public Integer getActivity() {return activity;}

    public void setActivity(@NonNull Integer activity) {this.activity = activity;}

    @NonNull
    public Integer getPurpose() {return purpose;}

    public void setPurpose(@NonNull Integer purpose) {this.purpose = purpose;}

    @Override
    public String toString() {
        return "HealthInfo{" +
                "userId='" + userId + '\'' +
                ", date=" + date +
                ", height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", gender=" + gender +
                ", activity=" + activity +
                ", purpose=" + purpose +
                '}';
    }
}
