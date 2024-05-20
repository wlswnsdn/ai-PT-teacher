package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Arrays;

// 외래키 설정
// foreignKeys = @ForeignKey(entity = User.class,
//        parentColumns = "avatarName", childColumns = "avatarName", onDelete = ForeignKey.CASCADE
@Entity(tableName = "avatar")
public class Avatar {

    public Avatar(@NonNull String avatarName, byte[] image, @NonNull Boolean isLocked, @NonNull String description) {
        this.avatarName = avatarName;
        this.image = image;
        this.isLocked = isLocked;
        this.description = description;
    }

    @NonNull
    @PrimaryKey
    private String avatarName;

    @ColumnInfo(name = "image")
    private byte[] image;

    // True -> 잠김 상태 False -> 열림 상태
    @NonNull
    @ColumnInfo(name = "isLocked")
    private Boolean isLocked;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    @NonNull
    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(@NonNull Boolean locked) {
        isLocked = locked;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "avatarName='" + avatarName + '\'' +
                ", image=" + Arrays.toString(image) +
                ", isLocked=" + isLocked +
                ", description='" + description + '\'' +
                '}';
    }
}
