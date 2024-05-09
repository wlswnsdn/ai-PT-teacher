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

    @NonNull
    @ColumnInfo(name = "Gender")
    private boolean gender;

    @NonNull
    @ColumnInfo(name = "Activity")
    private int activity;

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
}
