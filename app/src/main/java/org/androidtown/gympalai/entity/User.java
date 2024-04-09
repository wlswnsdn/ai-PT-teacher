package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(indices = {@Index(value = {"nickName"}, unique = true)})
public class User {
    @PrimaryKey
    private String userId;

    @NonNull
    @ColumnInfo(name = "pw")
    private String pw;

    @NonNull
    @ColumnInfo(name = "nickName")
    private String nickName;

    @ColumnInfo(name = "avatarName")
    private String avatarName;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getPw() {
        return pw;
    }

    public void setPw(@NonNull String pw) {
        this.pw = pw;
    }

    @NonNull
    public String getNickName() {
        return nickName;
    }

    public void setNickName(@NonNull String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }
}
