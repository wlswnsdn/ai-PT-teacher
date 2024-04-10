package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity
public class Avatar {
    @PrimaryKey
    private String avatarName;

    @ColumnInfo(name = "date")
    private LocalDateTime date;

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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
}
