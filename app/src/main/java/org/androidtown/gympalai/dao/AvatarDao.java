package org.androidtown.gympalai.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.androidtown.gympalai.entity.Avatar;

import java.util.List;

@Dao
public interface AvatarDao {

    @Query("select * from avatar")
    LiveData<List<Avatar>> getAll();

    @Query("select * from avatar where avatarName=:avatarName")
    Avatar getAvatar(String avatarName);

    @Query("select count(*) =0 from avatar")
    boolean isTableEmpty();

    @Insert
    void insert(Avatar avatar);

    @Update
    void update(Avatar avatar);

    @Delete
    void delete(Avatar avatar);
}
