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

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

}
