package org.androidtown.gympalai.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Arrays;

// nickName에 unique 설정
@Entity(tableName = "user", indices = {@Index(value = {"nickName"}, unique = true)})
public class User {

    public User(@NonNull String userId, @NonNull String pw, @NonNull String nickName, String avatarName, byte[] profilePicture) {
        this.userId = userId;
        this.pw = pw;
        this.nickName = nickName;
        this.avatarName = avatarName;
        this.profilePicture = profilePicture;
    }

    @NonNull
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
    @ColumnInfo(name = "profilePicture")
    private byte[] profilePicture;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", pw='" + pw + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarName='" + avatarName + '\'' +
                ", profilePicture=" + Arrays.toString(profilePicture) +
                '}';
    }
}
