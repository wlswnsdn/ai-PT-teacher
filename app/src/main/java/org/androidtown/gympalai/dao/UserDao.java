package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from user")
    LiveData<List<User>> getAll();

    @Query("select avatarName from user where userId=:userId")
    String getAvatarName(String userId);

    @Query("select nickName from user where userId=:userId")
    String getNickName(String userId);

    @Query("update user set avatarName=:avatarName where userId=:userId")
    void updateAvatar(String userId, String avatarName);

    @Insert
    void insert(User user); //회원등록

    @Update
    void update(User user);

    @Delete
    void delete(User user); // 회원 삭제

    @Query("SELECT userId FROM user")
    List<String> getUserIdList(); // 아이디값을 List<String>로 받아 회원가입 시 사전에 존재하는 아이디값인지 확인하기 위함
    @Query("SELECT nickName FROM user")
    List<String> getUserNickList();

    @Query("UPDATE user SET nickName = :newNickName WHERE userId = :userId")
    void updateNickName(String userId, String newNickName); //닉네임 변경을 위한 메소드
    @Query("Select nickName FROM user Where userId = :loginId")
    String getUserNickById(String loginId);

    @Query("UPDATE user SET pw = :newPw WHERE userId = :userId")
    void updatePw(String userId, String newPw); //비밀번호 변경을 위한 메소드

    @Query("SELECT pw FROM user WHERE userId = :loginId")
    String getUserPwById(String loginId); //로그인 시 아이디로 비밀번호를 가져와 일치여부를 판단할거임

}
