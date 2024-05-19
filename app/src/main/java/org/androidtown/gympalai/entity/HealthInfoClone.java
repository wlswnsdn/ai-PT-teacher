package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.androidtown.gympalai.converter.DateConverter;

import java.util.Date;

@Entity(tableName = "healthInfoClone", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE))
@TypeConverters({DateConverter.class})
public class HealthInfoClone {

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

    @NonNull
    @ColumnInfo(name = "Date")
    private Date date;

    public HealthInfoClone(@NonNull String userId, float height, float weight,
                      int age, boolean gender, int activity, int purpose, @NonNull Date date) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.activity = activity;
        this.purpose = purpose;
        this.date = date;
    }

    // Getter 및 Setter 메서드
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

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
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
                ", date=" + date +
                '}';
    }
}
