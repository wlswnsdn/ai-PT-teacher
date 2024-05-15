package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "healthInfo",foreignKeys = @ForeignKey(entity= User.class,
        parentColumns =  "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE))

public class HealthInfo {

    @NonNull
    @PrimaryKey
    private String userId;

    @NonNull
    @ColumnInfo(name = "Height")
    private float height;

    @NonNull
    @ColumnInfo(name = "Weight")
    private float weight;

    @NonNull
    @ColumnInfo(name = "Age")
    private int age;

    //true 남자, false 여자
    @NonNull
    @ColumnInfo(name = "Gender")
    private boolean gender;

    //0:거의 앉아있고 운동하지 않음, 1:일주일에 1~2회, 2:일주일에 3~5회, 3: 일주일에 6~7회, 4: 하루 2번 매우 심한 운동
    @NonNull
    @ColumnInfo(name = "Activity")
    private int activity;

    //0:다이어트, 1:유지어터, 2: 벌크업
    @NonNull
    @ColumnInfo(name = "Purpose")
    private int purpose;

    public HealthInfo(@NonNull String userId, float height, float weight,
                      int age, boolean gender, int activity, int purpose){
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.activity = activity;
        this.purpose = purpose;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        return "HealthInfo{" +
                "userId='" + userId + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", gender=" + gender +
                ", activity=" + activity +
                ", purpose=" + purpose +
                '}';
    }
}
